How to run the program:
1. Change your current directory to the one where all the folders and files are present (src/bin/Makefile)
2. There are two programs to run, the serial (MonteCarloMinimizarion) 
   and Parallel (MonteCarloMinimizationParallel), please type this as the parameter when you run the program
2. type in the terminal "java -cp bin MonteCarloMini/<type either the name of serial or parallel program> <then type all the parameters for the values in this order: rows columns xmin xmax ymin ymax search_density>
Example: 
java -cp bin MonteCarloMini.MonteCarloMinimizationParallel 1000 1000 -3500 3500 -3500 3500 0.5
java -cp bin MonteCarloMini.MonteCarloMinimization 1000 1000 -3500 3500 -3500 3500 0.5

If the java program ran out of Heap Space, type the following:
java -Xmx<heap space: say for instance "10G"> -cp bin MonteCarloMini.<Parallel or Serial program> <Parameters>

If you want to compile and run serial or parallel programs, do the following:
type in:
make run <serial or parallel> ARGS= "<rows columns xmin xmax ymin ymax search_density>