"""
 Example program to show using an array to back a grid on-screen.

 Sample Python/Pygame Programs
 Simpson College Computer Science
 http://programarcadegames.com/
 http://simpson.edu/computer-science/

 Explanation video: http://youtu.be/mdTeqiWyFnc
"""
import pygame
import time

# Define some colors
BLACK = (0, 0, 0)
WHITE = (255, 255, 255)
GREEN = (0, 255, 0)
RED = (255, 0, 0)
BLUE = (0, 0, 255)
YELLOW = (255, 255, 0)

# Define some sizes
# This sets the WIDTH and HEIGHT of each grid location
CELL_WIDTH = 20
CELL_HEIGHT = 20
# number of grids
CELL_COUNT_PER_SIDE = 20
# window size
WINDOW_WIDTH = CELL_WIDTH * CELL_COUNT_PER_SIDE
WINDOW_HEIGHT = CELL_HEIGHT * CELL_COUNT_PER_SIDE

# Define refresh interval
INTERVAL = 0.3

# Define names
CURRENT = 1
PASSED = 2
PADDING = 3
OBSTACLE = 4
CRASH = 5
NORTH = 'north'
SOUTH = 'south'
EAST = 'east'
WEST = 'west'
UP = '^'
DOWN = 'v'
RIGHT = '>'
LEFT = '<'

# This sets the margin between each cell
MARGIN = 1

# Create a 2 dimensional array. A two dimensional
# array is simply a list of lists.
pos_dir_list = [[[1, 1], 'north'], [[1, 2], 'north'], [[1, 3], 'north'], [[1, 3], 'east'], [[2, 3], 'east'],
                [[3, 3], 'east'], [[4, 3], 'east'], [[5, 3], 'east'], [[6, 3], 'east'], [[7, 3], 'east'],
                [[8, 3], 'east'], [[9, 3], 'east'], [[9, 3], 'south'], [[9, 2], 'south'], [[9, 1], 'south'],
                [[9, 1], 'west'], [[8, 1], 'west'], [[9, 1], 'west'], [[10, 1], 'west'], [[11, 1], 'west'],
                [[11, 1], 'north'], [[11, 2], 'north'], [[11, 3], 'north'], [[11, 3], 'west'], [[12, 3], 'west'],
                [[13, 3], 'west'], [[13, 3], 'south'], [[13, 4], 'south'], [[13, 5], 'south'], [[13, 6], 'south'],
                [[13, 7], 'south'], [[13, 8], 'south'], [[13, 9], 'south'], [[13, 10], 'south'], [[13, 11], 'south'],
                [[13, 12], 'south'], [[13, 12], 'west'], [[12, 12], 'west'], [[11, 12], 'west'], [[11, 12], 'south'],
                [[11, 11], 'south'], [[11, 11], 'west'], [[12, 11], 'west'], [[13, 11], 'west'], [[14, 11], 'west'],
                [[15, 11], 'west'], [[15, 11], 'south'], [[15, 12], 'south'], [[15, 13], 'south'], [[15, 14], 'south'],
                [[15, 15], 'south'], [[15, 15], 'west'], [[14, 15], 'west'], [[15, 15], 'west'], [[15, 15], 'south'],
                [[15, 16], 'south'], [[15, 17], 'south'], [[15, 17], 'west'], [[14, 17], 'west'], [[13, 17], 'west'],
                [[12, 17], 'west'], [[11, 17], 'west'], [[10, 17], 'west'], [[9, 17], 'west'], [[8, 17], 'west'],
                [[7, 17], 'west'], [[6, 17], 'west'], [[5, 17], 'west'], [[4, 17], 'west'], [[3, 17], 'west'],
                [[2, 17], 'west'], [[2, 17], 'south'], [[2, 16], 'south']]
obs_pos_dir_list = [[[5, 1], 'east'], [[11, 6], 'south'], [[11, 8], 'north'], [[11, 15], 'east'], [[2, 13], 'north']]
grid = []
dir = UP
for row in range(CELL_COUNT_PER_SIDE):
    # Add an empty array that will hold each cell
    # in this row
    grid.append([])
    for column in range(CELL_COUNT_PER_SIDE):
        grid[row].append(0)  # Append a cell

# Set row 1, cell 5 to one. (Remember rows and
# column numbers start at zero.)
for obs_pos_dir in obs_pos_dir_list:
    pos = obs_pos_dir[0]
    y = CELL_COUNT_PER_SIDE - pos[1] - 2
    x = pos[0]
    # dir
    if obs_pos_dir[1] == NORTH:
        dir = UP
    elif obs_pos_dir[1] == SOUTH:
        dir = DOWN
    elif obs_pos_dir[1] == EAST:
        dir = RIGHT
    else:
        dir = LEFT
    grid[y][x] = OBSTACLE

# Initialize pygame
pygame.init()

# Set the HEIGHT and WIDTH of the screen
WINDOW_SIZE = [WINDOW_WIDTH, WINDOW_HEIGHT]
screen = pygame.display.set_mode(WINDOW_SIZE)

# Set title of screen
pygame.display.set_caption("Algo Sim")

# Loop until the user clicks the close button.
done = False

# Used to manage how fast the screen updates
clock = pygame.time.Clock()

# -------- Main Program Loop -----------
ran = False
while not done:
    for event in pygame.event.get():  # User did something
        if event.type == pygame.QUIT:  # If user clicked close
            done = True  # Flag that we are done so we exit this loop
    if not ran:
        for pos_dir in pos_dir_list:
            pos = pos_dir[0]
            y = CELL_COUNT_PER_SIDE - pos[1] - 2
            x = pos[0]
            # dir
            if pos_dir[1] == NORTH:
                dir = UP
            elif pos_dir[1] == SOUTH:
                dir = DOWN
            elif pos_dir[1] == EAST:
                dir = RIGHT
            else:
                dir = LEFT
            if grid[y][x] == OBSTACLE:
                grid[y][x] = CRASH
            else:
                grid[y][x] = CURRENT
            for padding in [[-1, -1], [-1, 0], [-1, 1], [0, -1], [0, 1], [1, -1], [1, 0], [1, 1]]:
                if grid[y + padding[0]][x + padding[1]] != PASSED and grid[y + padding[0]][x + padding[1]] != CRASH:
                    grid[y + padding[0]][x + padding[1]] = PADDING
                if grid[y + padding[0]][x + padding[1]] == OBSTACLE:
                    grid[y + padding[0]][x + padding[1]] = CRASH

            # Draw the grid
            for row in range(CELL_COUNT_PER_SIDE):
                for column in range(CELL_COUNT_PER_SIDE):
                    color = WHITE
                    if grid[row][column] == CURRENT:
                        color = GREEN
                    elif grid[row][column] == PADDING:
                        color = BLACK
                    elif grid[row][column] == PASSED:
                        color = BLUE
                    elif grid[row][column] == OBSTACLE:
                        color = YELLOW
                    elif grid[row][column] == CRASH:
                        color = RED
                    pygame.draw.rect(screen,
                                     color,
                                     [(MARGIN + CELL_WIDTH) * column + MARGIN,
                                      (MARGIN + CELL_HEIGHT) * row + MARGIN,
                                      CELL_WIDTH,
                                      CELL_HEIGHT])
                    screen.blit(pygame.font.Font(None, 30).render(dir, True, RED),
                                (CELL_HEIGHT * (x) + CELL_HEIGHT / 2, CELL_WIDTH * (y + 1)))

                    # Limit to 60 frames per second
            clock.tick(60)

            # Go ahead and update the screen with what we've drawn.
            pygame.display.flip()
            time.sleep(INTERVAL)
            grid[y][x] = PASSED
        ran = True

# Be IDLE friendly. If you forget this line, the program will 'hang'
# on exit.
pygame.quit()