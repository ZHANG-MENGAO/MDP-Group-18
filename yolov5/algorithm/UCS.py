## Packages
from queue import PriorityQueue
from multiprocessing.pool import ThreadPool as Pool
from multiprocessing import cpu_count
import itertools
import numpy as np
import math
import copy
import socket
import json
import time

## Constants
NORTH = 'north'
SOUTH = 'south'
EAST = 'east'
WEST = 'west'
north = 'north'
south = 'south'
east = 'east'
west = 'west'
LEAST_COST = 'least cost'
LEAST_COST_PARENT = 'least cost parent'
MOVEMENT = 'movement'
FORWARD = 'forward'
BACKWARD = 'backward'
TURN_RIGHT_1 = 'turn right 1'
TURN_RIGHT_2 = 'turn right 2'
TURN_LEFT_1 = 'turn left 1'
TURN_LEFT_2 = 'turn left 2'
TURN_COST = 10
STRAIGHT_COST = 1
MAP_LENGTH_X = 20
MAP_LENGTH_Y = 20
STARTING_POS_DIR = [[1, 1], NORTH]
CAMERA_DISTANCE = 3
tr1 = 90  ## Right Turn r090
tl1 = 91  ## Left Turn l090
tl2 = 92  ## Reverse Right x090
tr2 = 93  ## Reverse Left z090
f = 94  ## Forward  f010(forward 10cm)
b = 95  ## Reverse b010 (back 10cm)
PATH = 'path'
COST = 'cost'


## Functions

def get_illegal_grids(map_length_x, map_length_y, obs_pos_dir_list):
    illegal_grids_set = set()  # {'[1, 0]', '[2, 0]'}

    # ----------- add boundary constraints -----------

    for i in range(map_length_x):
        illegal_grids_set.add(str([i, 0]))  # bottom
        illegal_grids_set.add(str([i, map_length_y - 1]))  # top
    for i in range(map_length_y):
        illegal_grids_set.add(str([0, i]))  # left
        illegal_grids_set.add(str([map_length_x - 1, i]))  # right

    # ----------- add obstacle constraints -----------

    for obs_pos_dir in obs_pos_dir_list:
        illegal_grids_set.add(str([obs_pos_dir[0][0] - 1, obs_pos_dir[0][1] + 1]))  # top left
        illegal_grids_set.add(str([obs_pos_dir[0][0], obs_pos_dir[0][1] + 1]))  # top mid
        illegal_grids_set.add(str([obs_pos_dir[0][0] + 1, obs_pos_dir[0][1] + 1]))  # top right
        illegal_grids_set.add(str([obs_pos_dir[0][0] - 1, obs_pos_dir[0][1]]))  # mid left
        illegal_grids_set.add(str([obs_pos_dir[0][0], obs_pos_dir[0][1]]))  # mid mid
        illegal_grids_set.add(str([obs_pos_dir[0][0] + 1, obs_pos_dir[0][1]]))  # mid right
        illegal_grids_set.add(str([obs_pos_dir[0][0] - 1, obs_pos_dir[0][1] - 1]))  # bottom left
        illegal_grids_set.add(str([obs_pos_dir[0][0], obs_pos_dir[0][1] - 1]))  # bottom mid
        illegal_grids_set.add(str([obs_pos_dir[0][0] + 1, obs_pos_dir[0][1] - 1]))  # bottom right

    return illegal_grids_set


def get_opp_direction(direction):
    if (direction == NORTH):
        return SOUTH
    elif (direction == SOUTH):
        return NORTH
    elif (direction == EAST):
        return WEST
    elif (direction == WEST):
        return EAST
    else:
        return 0


def get_reading_pos_dir_list(obs_pos_dir_list):
    reading_pos_dir_list = []
    obs_dir = None
    pos = []
    for obs_pos_dir in obs_pos_dir_list:
        # print(1,obs_pos_dir)
        obs_dir = obs_pos_dir[1]
        pos = obs_pos_dir[0]
        if obs_dir == NORTH:
            pos = [pos[0], pos[1] + CAMERA_DISTANCE]
        elif obs_dir == SOUTH:
            pos = [pos[0], pos[1] - CAMERA_DISTANCE]
        elif obs_dir == EAST:
            pos = [pos[0] + CAMERA_DISTANCE, pos[1]]
        else:
            pos = [pos[0] - CAMERA_DISTANCE, pos[1]]
        reading_pos_dir_list.append([pos, get_opp_direction(obs_dir)])
    return reading_pos_dir_list


def get_straight_movement(pos, dir, pos_moved):
    if (dir == NORTH and pos_moved[1] > pos[1] or dir == SOUTH and pos_moved[1] < pos[1] or
            dir == EAST and pos_moved[0] > pos[0] or dir == WEST and pos_moved[0] < pos[0]):
        return FORWARD
    else:
        return BACKWARD


def get_one_step_displacements(illegal_grids, pos_dir):
    pos_dir_next_list = []
    pos = pos_dir[0]
    dir = pos_dir[1]
    pos1 = None
    pos2 = None

    # robot facing north or south
    if (dir == SOUTH or dir == NORTH):
        pos1 = [pos[0], pos[1] + 1]
        pos2 = [pos[0], pos[1] - 1]
    # robot facing east or west
    else:
        pos1 = [pos[0] - 1, pos[1]]
        pos2 = [pos[0] + 1, pos[1]]

    # add to list if the new positions are not illegal
    if str(pos1) not in illegal_grids:
        pos_dir_next_list.append([pos1, dir, get_straight_movement(pos, dir, pos1)])
    if str(pos2) not in illegal_grids:
        pos_dir_next_list.append([pos2, dir, get_straight_movement(pos, dir, pos2)])

    return pos_dir_next_list


def get_turn_regions(pos_dir, turn):
    pos = pos_dir[0]
    dir = pos_dir[1]
    grids_required = []

    # global top right empty
    if (turn == TURN_RIGHT_1 and dir == NORTH or turn == TURN_RIGHT_2 and dir == WEST or
            turn == TURN_LEFT_2 and dir == SOUTH or turn == TURN_LEFT_1 and dir == EAST):
        for hori in range(3):
            for vert in range(3):
                grids_required.append(str([pos[0] + hori, pos[1] + vert]))
    # global top left empty
    elif (turn == TURN_LEFT_1 and dir == NORTH or turn == TURN_RIGHT_1 and dir == WEST or
          turn == TURN_RIGHT_2 and dir == SOUTH or turn == TURN_LEFT_2 and dir == EAST):
        for hori in range(3):
            for vert in range(3):
                grids_required.append(str([pos[0] - hori, pos[1] + vert]))
    # global bottom left empty
    elif (turn == TURN_LEFT_2 and dir == NORTH or turn == TURN_LEFT_1 and dir == WEST or
          turn == TURN_RIGHT_1 and dir == SOUTH or turn == TURN_RIGHT_2 and dir == EAST):
        for hori in range(3):
            for vert in range(3):
                grids_required.append(str([pos[0] - hori, pos[1] - vert]))
    # global bottom right empty
    else:
        for hori in range(3):
            for vert in range(3):
                grids_required.append(str([pos[0] + hori, pos[1] - vert]))

    return grids_required


def get_direction_after_turn(dir, turn):
    if (dir == NORTH and turn == TURN_LEFT_1 or dir == SOUTH and turn == TURN_RIGHT_2 or
            dir == NORTH and turn == TURN_LEFT_2 or dir == SOUTH and turn == TURN_RIGHT_1):
        return WEST
    elif (dir == NORTH and turn == TURN_RIGHT_1 or dir == SOUTH and turn == TURN_LEFT_1 or
          dir == NORTH and turn == TURN_RIGHT_2 or dir == SOUTH and turn == TURN_LEFT_2):
        return EAST
    elif (dir == WEST and turn == TURN_RIGHT_1 or dir == EAST and turn == TURN_LEFT_1 or
          dir == WEST and turn == TURN_RIGHT_2 or dir == EAST and turn == TURN_LEFT_2):
        return NORTH
    else:
        return SOUTH


def get_turn_displacements(illegal_grids, pos_dir):
    pos_dir_next_list = []
    grids_required = None
    valid = True

    # right turns
    for turn in [TURN_RIGHT_1, TURN_RIGHT_2]:
        valid = True
        grids_required = get_turn_regions(pos_dir, turn)
        # check if all grids are legal
        for grid in grids_required:
            if grid in illegal_grids:
                valid = False
                break
        # if all grids are legal, add to list and break
        if valid:
            pos_dir_next_list.append([pos_dir[0], get_direction_after_turn(pos_dir[1], turn), turn])
            break

    # left turns
    for turn in [TURN_LEFT_1, TURN_LEFT_2]:
        valid = True
        grids_required = get_turn_regions(pos_dir, turn)
        # check if all grids are legal
        for grid in grids_required:
            if grid in illegal_grids:
                valid = False
                break
        # if all grids are legal, add to list and break
        if valid:
            pos_dir_next_list.append([pos_dir[0], get_direction_after_turn(pos_dir[1], turn), turn])
            break

    return pos_dir_next_list
socket

def get_path_cost_movements(pos_dir_dict, pos_dir1, pos_dir2):
    path = []
    movements = []
    cost = pos_dir_dict[str(pos_dir2)][LEAST_COST]
    pos_dir = pos_dir2

    while pos_dir != pos_dir1:
        path.insert(0, pos_dir)
        movements.insert(0, pos_dir_dict[str(pos_dir)][MOVEMENT])
        pos_dir = pos_dir_dict[str(pos_dir)][LEAST_COST_PARENT]
    # path.insert(0, pos_dir)
    # print('path: ', path)
    # print('cost: ', cost)
    #     print('hey: ', movements)
    return path, cost, movements


def get_shortest_path_img_to_img(illegal_grids, pos_dir1, pos_dir2):
    queue = PriorityQueue()  # [(1,'[[1, 1], 'north']'), (1,'[[1, 2], 'north']')]
    visited = set()  # {'[[1, 1], 'north']'}
    pos_dir_dict = {}
    pos_dir = []
    turn_displacements = []
    one_step_displacements = []
    parent_cost = 0
    displacement_cost = 0
    displacement_str = ''
    pos_dir_str = ''
    path = []
    cost = 0
    movements = []

    queue.put((0, pos_dir1))
    pos_dir_str = str(pos_dir1)
    pos_dir_dict[pos_dir_str] = {LEAST_COST: 0, LEAST_COST_PARENT: pos_dir1}

    while not queue.empty():
        # print('queue size: ', queue.qsize())
        # get item with the lowest cost
        pos_dir = queue.get()[1]
        pos_dir_str = str(pos_dir)
        parent_cost = pos_dir_dict[pos_dir_str][LEAST_COST]

        # add to visited
        visited.add(pos_dir_str)

        # find next valid states
        turn_displacements = get_turn_displacements(illegal_grids, pos_dir)
        one_step_displacements = get_one_step_displacements(illegal_grids, pos_dir)

        # for all next valid states, get cost, if not visited or lower cost, modify pos_dir_dict and add to queue
        for displacement in turn_displacements + one_step_displacements:
            movement = displacement[2]
            #             print('movement: ', movement)
            displacement = [displacement[0], displacement[1]]
            if displacement[0] == pos_dir[0]:
                displacement_cost = parent_cost + TURN_COST
            else:
                displacement_cost = parent_cost + STRAIGHT_COST
            # print('displacement cost: ', displacement_cost)
            displacement_str = str(displacement)
            if displacement_str not in visited or displacement_cost < pos_dir_dict[displacement_str][LEAST_COST]:
                # print('movement: ', movement)
                pos_dir_dict[displacement_str] = {LEAST_COST: displacement_cost, LEAST_COST_PARENT: pos_dir,
                                                  MOVEMENT: movement}
                queue.put((displacement_cost, displacement))
            # for each valid displacement, if displacement == pos_dir2, return path
            if displacement == pos_dir2:
                path, cost, movements = get_path_cost_movements(pos_dir_dict, pos_dir1, pos_dir2)
                # print('path: ', path)
                # print('cost: ', cost)
                #                 print('movements: ' , movements)
                return path, cost, movements

    return 0, 0, 0


def algo_wrapper(args):
    return get_shortest_path_img_to_img(*args)


def rec_pos(obs_pos_dir_list):
    camera_distance = 3
    irec_pos = []
    for i in range(5):
        # print(obs_pos_dir_list)
        temp = obs_pos_dir_list[i][0].copy()
        irec_pos.append(temp)
        if (obs_pos_dir_list[i][1] == NORTH):
            irec_pos[i][1] += camera_distance
        elif (obs_pos_dir_list[i][1] == SOUTH):
            irec_pos[i][1] -= camera_distance
        elif (obs_pos_dir_list[i][1] == EAST):
            irec_pos[i][0] += camera_distance
        else:
            irec_pos[i][0] -= camera_distance
        # print(obs_pos_dir_list)
    return irec_pos


def Fastest5(rec_pos, obs_pos_dir_list):
    td = 0
    queue = []
    fqueue = []
    seq = [1, 2, 3, 4, 5]
    seqt = list(itertools.permutations(seq))
    tt = np.zeros((6, 6), float)  ##Finding distances between obstacles and Source
    robot_rec_pos = copy.deepcopy(rec_pos)
    robot_rec_pos.insert(0, [0, 0])
    for i in range(6):
        for j in range(6):
            if (i != j):
                tt[i][j] = round(math.sqrt((robot_rec_pos[i][0] - robot_rec_pos[j][0]) ** 2 + (
                            robot_rec_pos[i][1] - robot_rec_pos[j][1]) ** 2), 2)  # pythagoras
            else:
                tt[i][j] = 100  # diag elements
    # print(tt)
    for i in seqt:
        i = list(i)
        for j in range(5):
            td += tt[j][j + 1]
        queue.append([td, i])
        queue = sorted(queue)
        if (len(queue) > 5):
            queue.pop(5)
    for i in range(5):
        fqueue.append(queue[i][1])
    # print(fqueue)
    dt = []
    for i in range(5):
        d = []
        for j in fqueue[i]:
            d.append(obs_pos_dir_list[j - 1])
        dt.append(d)
    return dt, fqueue


# right turn - r090
# left turn - l090
# reverse to face left - z090
# reverse to face right - x090

def convert_instruct(best_full_movements):
    instruction = copy.deepcopy(best_full_movements)
    for i in range(len(instruction)):  ## [forward,forward,backward]
        for j in range(len(instruction[i])):
            if (instruction[i][j] == 'forward'):
                instruction[i][j] = [1, f]
            elif (instruction[i][j] == 'backward'):
                instruction[i][j] = [1, b]
            elif (instruction[i][j] == 'turn right 1'):  ## turn right (face right)
                instruction[i][j] = [tr1]
            elif (instruction[i][j] == 'turn left 1'):  ## turn left (face left)
                instruction[i][j] = [tl1]
            elif (instruction[i][j] == 'turn left 2'):  ## reverse right (face left)
                instruction[i][j] = [tl2]
            elif (instruction[i][j] == 'turn right 2'):  ## reverse left (face right)
                instruction[i][j] = [tr2]
            else:
                print("error")

    return instruction


def redundant_remove(instruct):
    n_instruct = []
    j = 0
    for i in range(len(instruct)):  ## 5 iterations (obstacles)
        j = 0
        temp = []
        if (len(instruct[i]) == 1):  ##single instruction
            temp.append(instruct[i][j])
        while (j < len(instruct[i]) - 1):  ## based on number of instruction in from 1 obstacle to another
            if (len(instruct[i][j]) == 2):
                direc = instruct[i][j][1]
                dist = instruct[i][j][0]
                while (len(instruct[i][j + 1]) == 2):
                    if (direc == instruct[i][j + 1][1]):
                        dist += instruct[i][j + 1][0]
                    else:
                        dist -= instruct[i][j + 1][0]
                    if (dist < 0):
                        if (instruct[i][j][1] == 94):
                            direc = 95
                        else:
                            direc = 94
                    j += 1
                    if (j == len(instruct[i]) - 1):
                        break
                temp.append([dist, direc])
            elif ((instruct[i][j][0] == tr1 or instruct[i][j][0] == tr2) and (
                    instruct[i][j + 1][0] == tl1 or instruct[i][j + 1][0] == tl2)):
                j += 1
            elif ((instruct[i][j][0] == tl1 or instruct[i][j][0] == tl2) and (
                    instruct[i][j + 1][0] == tr1 or instruct[i][j + 1][0] == tr2)):
                j += 1
            else:
                temp.append(instruct[i][j])
            j += 1
            if (j == len(instruct[i]) - 1):  ## if last element
                temp.append(instruct[i][j])

        n_instruct.append(temp)
    return n_instruct


def robot_instruct(tempI):
    r_instruct = "STMI,"
    for i in range(len(tempI)):
        for j in range(len(tempI[i])):
            if (len(tempI[i][j]) == 2):
                c = tempI[i][j][0] * 10
                d = tempI[i][j][1]
                if (d == f):
                    if (c >= 100):
                        r_instruct += "f" + str(c) + ","
                    elif (c < 100 and c > 0):
                        r_instruct += "f0" + str(c) + ","
                else:
                    if (c >= 100):
                        r_instruct += "b" + str(c) + ","
                    elif (c < 100 and c > 0):
                        r_instruct += "b0" + str(c) + ","

            else:
                if (tempI[i][j][0] == tr1):
                    r_instruct += "r090" + ","
                elif (tempI[i][j][0] == tl1):
                    r_instruct += "l090" + ","
                elif (tempI[i][j][0] == tl2):
                    r_instruct += "z090" + ","
                elif (tempI[i][j][0] == tr2):
                    r_instruct += "x090" + ","
        r_instruct += 'obs' + ","
    f_instruct = r_instruct[:len(r_instruct) - 1]
    return f_instruct

#   instruct_trial = 'SIMI, f010, l090, f010, r090, b010, l090, b010, r090, obs,'

def rpi_obs_seq(fp):
    rpi_seq = "IMAGE|"
    rpi_seq += str(fp)
    return rpi_seq


def get_best_obs_id_seq(best_sequence, ireading_pos_dir_list):
    best_sequence.pop(0)
    # print(best_sequence)
    id_sequence = []
    for item in best_sequence:
        id_sequence.append(ireading_pos_dir_list.index(item) + 1)
    return id_sequence


# receive android inputs
def read_android_inputs():
    while (True):
        recvMsg = s.recv(1024).decode("UTF-8")
        return (recvMsg)


# parse obstacle positions from android
def parse_obstacle_pos_android(info_string):
    # parse data
    obstacles_info = json.loads(info_string.split(' Robot')[0].split('ObsOnMap ')[1])
    robot_info = json.loads(info_string.split('Robot ')[1])
    #     print(obstacles_info,"OI")
    #     print(robot_info,"RI")

    # sort obstacles info
    obstacles_info.sort(key=lambda x: x["obs_id"])
    # print("check obstacles info: ", obstacles_info)
    # print("check robot info: ", robot_info)

    # initialize
    robot_starting_pos_dir = []
    obs_pos_dir_list = []

    # get robot direction
    if robot_info["direction"] == "up":
        robot_direc = NORTH
    elif robot_info["direction"] == "down":
        robot_direc = SOUTH
    elif robot_info["direction"] == "left":
        robot_direc = WEST
    else:
        robot_direc = EAST

    # set robot position direction
    robot_starting_pos_dir = [[int(robot_info["x"]), int(robot_info["y"])], robot_direc]

    # add obstacle positions to list
    for i in range(len(obstacles_info)):
        print(obstacles_info[i])
        if obstacles_info[i]["direction"] == "top":
            obs_direc = NORTH
        elif obstacles_info[i]["direction"] == "bottom":
            obs_direc = SOUTH
        elif obstacles_info[i]["direction"] == "left":
            obs_direc = WEST
        else:
            obs_direc = EAST
        obs_pos_dir_list.append([[obstacles_info[i]["x"], obstacles_info[i]["y"]], obs_direc])

    # print('robot pos read: ', robot_pos)
    # print('robot direc read: ', robot_direc)
    # print('robot and obs pos read: ', robot_obs_pos)
    # print('robot and obs direc read: ', robot_obs_direc)

    return robot_starting_pos_dir, obs_pos_dir_list


# send to android every x seconds
def periodic_send_positions(interval, positions_directions):
    for i in range(len(positions_directions)):
        output_to_android = format_location_output(positions_directions[i][0], positions_directions[i][1])
        print(output_to_android)
        s.send(output_to_android.encode("UTF-8"))
        time.sleep(interval)


# format positions for android
def format_location_output(robot_pos, robot_direc):
    x_axis = int(robot_pos[0])
    y_axis = int(robot_pos[1])
    direction = None
    if (robot_direc == north):
        direction = "N"
    elif (robot_direc == south):
        direction = "S"
    elif (robot_direc == east):
        direction = "E"
    else:
        direction = "W"
    return 'ANDROID|ROBOT,<{x}>,<{y}>,<{dir}>'.format(x=x_axis, y=y_axis, dir=direction)


# get all positions of a straight line instruction
def get_straight_positions(instruction, robot_pos, robot_direc):
    positions = []
    steps, move_direction = instruction[0], instruction[1]
    if (robot_direc == north and move_direction == f) or (robot_direc == south and move_direction == b):
        for i in range(steps):
            positions.append([[robot_pos[0], robot_pos[1] + 1 + i], robot_direc])
        pos = [robot_pos[0], robot_pos[1] + steps]
    elif (robot_direc == south and move_direction == f) or (robot_direc == north and move_direction == b):
        for i in range(steps):
            positions.append([[robot_pos[0], robot_pos[1] - 1 - i], robot_direc])
        pos = [robot_pos[0], robot_pos[1] - steps]
    elif (robot_direc == east and move_direction == f) or (robot_direc == west and move_direction == b):
        for i in range(steps):
            positions.append([[robot_pos[0] + i + 1, robot_pos[1]], robot_direc])
        pos = [robot_pos[0] + steps, robot_pos[1]]
    else:
        for i in range(steps):
            positions.append([[robot_pos[0] - i - 1, robot_pos[1]], robot_direc])
        pos = [robot_pos[0] - steps, robot_pos[1]]
    # print(positions, pos, robot_direc)
    return positions, pos, robot_direc


# get all positions of a turn or special turn instruction
def get_turn_positions(instruction, robot_pos, robot_direc):
    positions_directions = []
    turn = instruction[0]
    rd = None  # robot direction

    # settle direction
    if turn == tr1 or turn == tr2:
        if (robot_direc == north):
            rd = east
        elif (robot_direc == south):
            rd = west
        elif (robot_direc == east):
            rd = south
        else:
            rd = north
    elif turn == tl1 or turn == tl2:
        if (robot_direc == north):
            rd = west
        elif (robot_direc == south):
            rd = east
        elif (robot_direc == east):
            rd = north
        else:
            rd = south
    else:
        rd = robot_direc

    # settle positions directions
    for i in range(TURN_COST):
        positions_directions.append([robot_pos, rd])

    return positions_directions, robot_pos, rd


# get all positions from instructions for android
def get_positions(instructs, start_pos, start_direc):
    positions_directions = []
    curr_pos = start_pos
    curr_direc = start_direc
    for stage in instructs:
        for instruct in stage:
            # straight line instruction
            if len(instruct) == 2:
                # update positions_directions, curr_pos, curr_direc
                pd, cp, cd = get_straight_positions(instruct, curr_pos, curr_direc)
            # turns
            else:
                pd, cp, cd = get_turn_positions(instruct, curr_pos, curr_direc)
            positions_directions.extend(pd)
            curr_pos = cp
            curr_direc = cd
    return positions_directions


def get_img2img_str(pos_dir1, pos_dir2):
    return str([pos_dir1, pos_dir2])


## Actual Run
start = time.time()
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# include the new code
while True:
    try:
        print('trying')
        s.connect(("192.168.18.1", 2763))
        print('connected!')
        break
    except:
        continue
# trial GROUP 18
instruct_trial_stm = 'STMI,f050'
instruct_trial = 'STMI,f010,a090,f020'
instruct_trial1 = 'STMI,f010,a085,obs,1'
#instruct_trial2 = 'STMI,b020,a085,obs,5'
instruct_trial_coor = 'ANDROID|ROBOT,12,13,N:ROBOT,11,14,W:ROBOT,9,14,W'
instruct_trial_coor1 = 'ANDROID|ROBOT,8,14,W:ROBOT,7,13,S'
#instruct_trial_obsID = 'IMAGE|6'

s.send(instruct_trial_stm.encode("UTF-8"))
time.sleep(2)
s.send(instruct_trial_coor.encode("UTF-8"))
num = 1
# while True:
#     recvMsg = s.recv(1024).decode("UTF-8")
#     print('msg received: ', recvMsg)
#     if recvMsg == 'g':
#
#         if num == 1:
#         # print('everything is gd')
#         # instruct_trial1 = 'STMI,a090,d090'
#             s.send(instruct_trial1.encode("UTF-8"))
#             time.sleep(2)
#             s.send(instruct_trial_coor1.encode("UTF-8"))
#
#         if num == 2:
#             pass
#             #s.send(instruct_trial2.encode("UTF-8"))
#     num += 1

#s.send(instruct_trial1.encode("UTF-8"))
# trial GROUp 18

# s.connect(("192.168.3.1",2763))

# 1. read from android
info_string = read_android_inputs()

# info_string = 'ObsOnMap [{"x":1,"y":7,"direction":"bottom","obs_id":1},{"x":5,"y":7,"direction":"bottom","obs_id":2},{"x":6,"y":8,"obs_id":-1},{"x":6,"y":9,"direction":"right","obs_id":3},{"x":17,"y":12,"direction":"left","obs_id":4},{"x":8,"y":19,"direction":"bottom","obs_id":5}] Robot {"x":"1","y":"1","direction":"up"}'
print(info_string)

# 2. process data to get robot and obs pos dir
robot_starting_pos_dir, obs_pos_dir_list = parse_obstacle_pos_android(info_string)
# robot_starting_pos_dir = STARTING_POS_DIR
# obs_pos_dir_list = [[[5, 1], EAST], [[11, 6], SOUTH], [[11, 8], NORTH], [[11, 15], EAST], [[2, 13], NORTH]] # test set 7
# print('robot_starting_pos_dir: ', robot_starting_pos_dir)
print('obs_pos_dir_list: ', obs_pos_dir_list)

# 3. run search algo to get obs_id_seq for rpi, robot pos dir for android, commands for robot
# 3.1 get illegal grids
illegal_grids = get_illegal_grids(MAP_LENGTH_X, MAP_LENGTH_Y, obs_pos_dir_list)
# print('illegal_grids: ', illegal_grids)

# 3.2 get best 5 reading positions and directions
# irec_pos = rec_pos(obs_pos_dir_list)
# f5_obs_seq = Fastest5(irec_pos,obs_pos_dir_list)
# print(2,f5_obs_seq)
# print(3,fp)
ireading_pos_dir_list = get_reading_pos_dir_list(obs_pos_dir_list)
dicktionary = {}
pos_dir_pair = []
for i, pos_dir1 in enumerate([robot_starting_pos_dir] + ireading_pos_dir_list):
    for pos_dir2 in ireading_pos_dir_list:
        if pos_dir1 != pos_dir2:
            #             print(pos_dir1)
            #             print(pos_dir2)
            pos_dir_pair.append((illegal_grids, pos_dir1, pos_dir2))
start = time.time()
core_count = cpu_count()
# print('core_count: ', core_count)
# print('len: ', len(pos_dir_pair))
with Pool(1) as pool:
    result = pool.map(algo_wrapper, pos_dir_pair)
    pool.terminate()
    pool.join()

    for i, item in enumerate(pos_dir_pair):
        dicktionary[get_img2img_str(item[1], item[2])] = {PATH: result[i][0], COST: result[i][1],
                                                          MOVEMENT: result[i][2]}
# print('end: ', time.time()-start)
# reading_pos_dir_list_permutations = []
# for obs_seq in f5_obs_seq[0]:
#     print(obs_seq,"obs_seq")
#     reading_pos_dir_list_permutations.append(get_reading_pos_dir_list(obs_seq))
reading_pos_dir_list_permutations = list(itertools.permutations(ireading_pos_dir_list))  # 120 permutations
# print('reading_pos_dir_list_permutations: ', reading_pos_dir_list_permutations)
# print('ireading_pos_dir_list: ', ireading_pos_dir_list)

# 3.3 get best path and sequence
best_full_path_cost = 0
for reading_pos_dir_list1 in reading_pos_dir_list_permutations:
    reading_pos_dir_list = list(reading_pos_dir_list1).copy()
    full_path_cost = 0
    full_path = [robot_starting_pos_dir]
    full_movements = []

    # add starting position
    reading_pos_dir_list.insert(0, robot_starting_pos_dir)
    last_node = robot_starting_pos_dir
    reading_pos_dir_list_final = [robot_starting_pos_dir]
    for i in range(len(reading_pos_dir_list) - 1):
        # get path and cost
        img2img_str = get_img2img_str(last_node, reading_pos_dir_list[(i + 1) % len(reading_pos_dir_list)])
        path = dicktionary[img2img_str][PATH]
        cost = dicktionary[img2img_str][COST]
        movement = dicktionary[img2img_str][MOVEMENT]
        # print('path: ', path)
        # print('cost: ', cost)
        if path != 0:
            last_node = reading_pos_dir_list[(i + 1) % len(reading_pos_dir_list)]
            full_path_cost = full_path_cost + cost
            full_path = full_path + path
            full_movements.append(movement)
            reading_pos_dir_list_final.append(reading_pos_dir_list[(i + 1) % len(reading_pos_dir_list)])

            # if path cost is better than best cost, update best cost and best path
    if best_full_path_cost == 0 or full_path_cost < best_full_path_cost:
        best_full_path_cost = full_path_cost
        best_full_path = full_path
        best_sequence = reading_pos_dir_list_final
        best_full_movements = full_movements

print('best_full_path: ', best_full_path)
print('best_full_path_cost: ', best_full_path_cost)
print('best_sequence: ', best_sequence)
print('best_full_movements: ', best_full_movements)

# 3.4 for rpi
obs_id_seq = get_best_obs_id_seq(best_sequence, ireading_pos_dir_list)
rpi_seq = rpi_obs_seq(obs_id_seq)
print(rpi_seq, 'rpi_seq')
# print('ireading_pos_dir_list: ', ireading_pos_dir_list)
# print('obs_id_seq: ', obs_id_seq)

# 3.5 for android
instructs = convert_instruct(best_full_movements)
r_instruct = redundant_remove(instructs)
# print('instructs: ', instructs)
positions_directions = []
positions_directions.extend(get_positions(r_instruct, [1, 1], north))

# 3.6 for robot
robI = robot_instruct(r_instruct)
print(robI, "robI")

# 4. send data
# send to rpi image rec
s.send(rpi_seq.encode("UTF-8"))



# send to robot
s.send(robI.encode("UTF-8"))
# send to android
time.sleep(1)
periodic_send_positions(1, positions_directions)
print(time.time() - start)
