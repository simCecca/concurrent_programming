# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.8

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /home/simone/Scrivania/clion-2017.2.3/bin/cmake/bin/cmake

# The command to remove a file.
RM = /home/simone/Scrivania/clion-2017.2.3/bin/cmake/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/simone/CLionProjects/HWC1

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/simone/CLionProjects/HWC1/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/HWC1.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/HWC1.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/HWC1.dir/flags.make

CMakeFiles/HWC1.dir/main.c.o: CMakeFiles/HWC1.dir/flags.make
CMakeFiles/HWC1.dir/main.c.o: ../main.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/simone/CLionProjects/HWC1/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/HWC1.dir/main.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/HWC1.dir/main.c.o   -c /home/simone/CLionProjects/HWC1/main.c

CMakeFiles/HWC1.dir/main.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/HWC1.dir/main.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/simone/CLionProjects/HWC1/main.c > CMakeFiles/HWC1.dir/main.c.i

CMakeFiles/HWC1.dir/main.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/HWC1.dir/main.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/simone/CLionProjects/HWC1/main.c -o CMakeFiles/HWC1.dir/main.c.s

CMakeFiles/HWC1.dir/main.c.o.requires:

.PHONY : CMakeFiles/HWC1.dir/main.c.o.requires

CMakeFiles/HWC1.dir/main.c.o.provides: CMakeFiles/HWC1.dir/main.c.o.requires
	$(MAKE) -f CMakeFiles/HWC1.dir/build.make CMakeFiles/HWC1.dir/main.c.o.provides.build
.PHONY : CMakeFiles/HWC1.dir/main.c.o.provides

CMakeFiles/HWC1.dir/main.c.o.provides.build: CMakeFiles/HWC1.dir/main.c.o


CMakeFiles/HWC1.dir/buffer.c.o: CMakeFiles/HWC1.dir/flags.make
CMakeFiles/HWC1.dir/buffer.c.o: ../buffer.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/simone/CLionProjects/HWC1/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building C object CMakeFiles/HWC1.dir/buffer.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/HWC1.dir/buffer.c.o   -c /home/simone/CLionProjects/HWC1/buffer.c

CMakeFiles/HWC1.dir/buffer.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/HWC1.dir/buffer.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/simone/CLionProjects/HWC1/buffer.c > CMakeFiles/HWC1.dir/buffer.c.i

CMakeFiles/HWC1.dir/buffer.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/HWC1.dir/buffer.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/simone/CLionProjects/HWC1/buffer.c -o CMakeFiles/HWC1.dir/buffer.c.s

CMakeFiles/HWC1.dir/buffer.c.o.requires:

.PHONY : CMakeFiles/HWC1.dir/buffer.c.o.requires

CMakeFiles/HWC1.dir/buffer.c.o.provides: CMakeFiles/HWC1.dir/buffer.c.o.requires
	$(MAKE) -f CMakeFiles/HWC1.dir/build.make CMakeFiles/HWC1.dir/buffer.c.o.provides.build
.PHONY : CMakeFiles/HWC1.dir/buffer.c.o.provides

CMakeFiles/HWC1.dir/buffer.c.o.provides.build: CMakeFiles/HWC1.dir/buffer.c.o


CMakeFiles/HWC1.dir/buffer_test.c.o: CMakeFiles/HWC1.dir/flags.make
CMakeFiles/HWC1.dir/buffer_test.c.o: ../buffer_test.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/simone/CLionProjects/HWC1/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Building C object CMakeFiles/HWC1.dir/buffer_test.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/HWC1.dir/buffer_test.c.o   -c /home/simone/CLionProjects/HWC1/buffer_test.c

CMakeFiles/HWC1.dir/buffer_test.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/HWC1.dir/buffer_test.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/simone/CLionProjects/HWC1/buffer_test.c > CMakeFiles/HWC1.dir/buffer_test.c.i

CMakeFiles/HWC1.dir/buffer_test.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/HWC1.dir/buffer_test.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/simone/CLionProjects/HWC1/buffer_test.c -o CMakeFiles/HWC1.dir/buffer_test.c.s

CMakeFiles/HWC1.dir/buffer_test.c.o.requires:

.PHONY : CMakeFiles/HWC1.dir/buffer_test.c.o.requires

CMakeFiles/HWC1.dir/buffer_test.c.o.provides: CMakeFiles/HWC1.dir/buffer_test.c.o.requires
	$(MAKE) -f CMakeFiles/HWC1.dir/build.make CMakeFiles/HWC1.dir/buffer_test.c.o.provides.build
.PHONY : CMakeFiles/HWC1.dir/buffer_test.c.o.provides

CMakeFiles/HWC1.dir/buffer_test.c.o.provides.build: CMakeFiles/HWC1.dir/buffer_test.c.o


CMakeFiles/HWC1.dir/msg_string.c.o: CMakeFiles/HWC1.dir/flags.make
CMakeFiles/HWC1.dir/msg_string.c.o: ../msg_string.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/simone/CLionProjects/HWC1/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_4) "Building C object CMakeFiles/HWC1.dir/msg_string.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/HWC1.dir/msg_string.c.o   -c /home/simone/CLionProjects/HWC1/msg_string.c

CMakeFiles/HWC1.dir/msg_string.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/HWC1.dir/msg_string.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/simone/CLionProjects/HWC1/msg_string.c > CMakeFiles/HWC1.dir/msg_string.c.i

CMakeFiles/HWC1.dir/msg_string.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/HWC1.dir/msg_string.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/simone/CLionProjects/HWC1/msg_string.c -o CMakeFiles/HWC1.dir/msg_string.c.s

CMakeFiles/HWC1.dir/msg_string.c.o.requires:

.PHONY : CMakeFiles/HWC1.dir/msg_string.c.o.requires

CMakeFiles/HWC1.dir/msg_string.c.o.provides: CMakeFiles/HWC1.dir/msg_string.c.o.requires
	$(MAKE) -f CMakeFiles/HWC1.dir/build.make CMakeFiles/HWC1.dir/msg_string.c.o.provides.build
.PHONY : CMakeFiles/HWC1.dir/msg_string.c.o.provides

CMakeFiles/HWC1.dir/msg_string.c.o.provides.build: CMakeFiles/HWC1.dir/msg_string.c.o


# Object files for target HWC1
HWC1_OBJECTS = \
"CMakeFiles/HWC1.dir/main.c.o" \
"CMakeFiles/HWC1.dir/buffer.c.o" \
"CMakeFiles/HWC1.dir/buffer_test.c.o" \
"CMakeFiles/HWC1.dir/msg_string.c.o"

# External object files for target HWC1
HWC1_EXTERNAL_OBJECTS =

HWC1: CMakeFiles/HWC1.dir/main.c.o
HWC1: CMakeFiles/HWC1.dir/buffer.c.o
HWC1: CMakeFiles/HWC1.dir/buffer_test.c.o
HWC1: CMakeFiles/HWC1.dir/msg_string.c.o
HWC1: CMakeFiles/HWC1.dir/build.make
HWC1: CMakeFiles/HWC1.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/simone/CLionProjects/HWC1/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_5) "Linking C executable HWC1"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/HWC1.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/HWC1.dir/build: HWC1

.PHONY : CMakeFiles/HWC1.dir/build

CMakeFiles/HWC1.dir/requires: CMakeFiles/HWC1.dir/main.c.o.requires
CMakeFiles/HWC1.dir/requires: CMakeFiles/HWC1.dir/buffer.c.o.requires
CMakeFiles/HWC1.dir/requires: CMakeFiles/HWC1.dir/buffer_test.c.o.requires
CMakeFiles/HWC1.dir/requires: CMakeFiles/HWC1.dir/msg_string.c.o.requires

.PHONY : CMakeFiles/HWC1.dir/requires

CMakeFiles/HWC1.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/HWC1.dir/cmake_clean.cmake
.PHONY : CMakeFiles/HWC1.dir/clean

CMakeFiles/HWC1.dir/depend:
	cd /home/simone/CLionProjects/HWC1/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/simone/CLionProjects/HWC1 /home/simone/CLionProjects/HWC1 /home/simone/CLionProjects/HWC1/cmake-build-debug /home/simone/CLionProjects/HWC1/cmake-build-debug /home/simone/CLionProjects/HWC1/cmake-build-debug/CMakeFiles/HWC1.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/HWC1.dir/depend

