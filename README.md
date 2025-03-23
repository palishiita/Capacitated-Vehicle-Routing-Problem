# Capacitated-Vehicle-Routing-Problem
## Implementation and Study of Evolutionary Algorithms Compared to Non-evolutionary Methods in CVRP

### **Problem Overview**
The **Capacitated Vehicle Routing Problem (CVRP)** is a combinatorial optimization challenge where a fleet of vehicles must deliver goods to a set of customers (cities) from a central **depot**. Each vehicle has a fixed **capacity**, and each customer has a **demand**. 

### Objective:
- Determine routes for each vehicle such that:
  - All customers are visited **exactly once**.
  - The total demand on each route does **not exceed the vehicle’s capacity**.
  - All routes **start and end at the depot**.
  - The **total distance traveled** is **minimized**.

CVRP is a generalization of the **Travelling Salesman Problem (TSP)** and is classified as **NP-hard**, meaning it requires heuristic or metaheuristic methods for large instances.

---

### **I. Genetic Algorithm (GA)**
An **evolutionary algorithm** inspired by natural selection. GA evolves a population of solutions over generations using:
- **Selection**: Choosing fitter individuals for reproduction. Common strategies include:
  - **Tournament Selection**: Randomly selecting a group and choosing the best among them.
  - **Roulette Wheel Selection**: Probabilistically selecting individuals based on fitness.
- **Crossover (Recombination)**: Combining parts of two parents to create offspring, encouraging **exploration** of new solution spaces.
- **Mutation**: Introducing random changes to maintain **genetic diversity** and prevent **premature convergence**.
- **Elitism**: Preserving the **best individuals** unchanged across generations to ensure quality solutions aren't lost.
- **Fitness Function**: Evaluates how well a solution performs relative to the objective, guiding the **selection process**.
- **Population**: A diverse set of candidate solutions that evolves over time.
- GA is effective at **global search** and escaping **local optima**, making it suitable for **complex problems like CVRP**.
- **Reference**: 

### Auxilary Questions:
1. How does selection (tournament size Tour) affect the operation of EA?
- **Tournament selection** controls **selection pressure**.
- **Smaller Tour (e.g., 1)**: More random selection → **greater diversity**, slower convergence.
- **Larger Tour (e.g., pop size)**: Always selects the best → **faster convergence**, but risk of **premature convergence**.

2. How do the EA’s behaviors differ with Tour=1 and Tour>1?
- **Tour = 1**: EA acts like **random search**, minimal selection pressure.
- **Tour > 1**: Introduces **competition** between individuals, guiding evolution towards **better solutions**.

3. Does the rule apply correctly to the CVRP problem?
- Yes. Selection, crossover, and mutation operators are **tailored to CVRP**:
- Routes are evolved while **respecting vehicle capacity**.
- Fitness evaluates **valid routes** based on **total distance**, including depot returns.

4. Can mutation be too small/large?
- **Too small**: Insufficient exploration → EA may **stagnate** in local optima.
- **Too large**: Excessive randomness → EA behaves like **random search**, losing good traits.

5. What role does the mutation operator play?
- Introduces **diversity** into the population.
- Helps EA **escape local optima**.
- Ensures the search space is **thoroughly explored**.

6. Can crossover be too small/large?
- **Low crossover rate**: EA cannot effectively **combine good solutions**.
- **High crossover rate**: Can be **disruptive** if not paired with **good selection**.

7. What role does the crossover operator play?
- Combines **building blocks** from parents.
- Enables EA to **exploit known good traits** and explore **new combinations**.
- Essential for **progressive improvement**.

8. What happens if we turn off crossover and/or mutation?

    | **Configuration** | **Outcome** |
    |-------------------|-----------------------------------------------------|
    | **No Crossover**  | Only mutation drives change → **slow convergence**. |
    | **No Mutation**   | Loss of diversity → **premature convergence**.      |
    | **Both Off**      | EA becomes **static**, like random search.          |


9. Is elitist selection necessary and beneficial?
Yes:
- Ensures the **best solution survives** each generation.
- Prevents **regression**.
- Small elitism (e.g., **1 best individual**) balances **exploration and exploitation**.

10. How does population size and number of offspring affect EA?

      | Parameter         | Effect                                                      |
      |-------------------|-------------------------------------------------------------|
      | **Larger Population** | Better **diversity**, higher chance to find global optima. |
      | **More Generations**  | Longer evolution → **better solutions**, more evaluations. |
      | **Balanced Setup**    | Pop Size = 100, Generations = 100 → **effective and efficient**. |

---

### **II. Greedy Search**
- The **Greedy Algorithm** builds a solution step-by-step by **always selecting the nearest unvisited city** that can be served without exceeding the vehicle’s capacity. Starting from the depot, it iteratively adds the closest feasible city to the route. If no such city can be added due to capacity limits, the vehicle returns to the depot, resets its load, and continues. This process repeats until all cities are visited. The resulting route is evaluated for its total distance (fitness), including all depot returns. 
- It **starts at the depot** and visits the nearest feasible customer.
- If no city can be visited due to capacity limits, the vehicle **returns to the depot** and continues.
- It is **fast** and **deterministic** but often **gets stuck in local optima** due to short-sighted decisions.
- Used for **baseline comparison** with more advanced algorithms.
- **Explanation**: https://www.youtube.com/watch?v=lfQvPHGtu6Q, https://www.youtube.com/watch?v=l4PlEaUo7_M  
- **Reference**: https://vss2sn.github.io/cvrp/algorithm_implementations.html#genetic-algorithm-ga 

---

### **III. Random Search**
- The **Random Search** algorithm generates a series of random city visit sequences and selecting the best solution based on total distance traveled (fitness). For a specified number of evaluations, it shuffles the list of cities to create random routes, evaluates each route using the problem’s fitness function (which accounts for vehicle capacity and depot returns), and tracks the shortest route found.
- Requires **no learning or adaptation** — purely **exploratory**.
- Performance depends entirely on **number of evaluations**.
- Useful for establishing a **performance baseline** to highlight the benefits of more intelligent algorithms like GA or Simulated Annealing.

---

### **IV. Simulated Annealing (SA)**
Absolutely! Here’s your **Simulated Annealing explanation** rephrased in the same **bullet-point style** as the one you provided, for clarity and consistency:
- A **metaheuristic inspired by metallurgy**, where materials are cooled slowly to reach **low-energy (optimal) states**.
- Starts with a **random initial solution** and iteratively applies **small changes** (e.g., swapping two cities in the route).
- **Worse solutions may be accepted** early on based on a **probabilistic acceptance function** tied to temperature, allowing escape from **local optima**.
- The **temperature decreases gradually** (cooling schedule), reducing the likelihood of accepting worse solutions over time.
- Strikes a balance between **exploration (diversity)** and **exploitation (refinement)**, often finding **high-quality solutions** with **fewer evaluations** compared to Genetic Algorithms.
- **Explanation**: https://www.youtube.com/watch?v=FyyVbuLZav8, https://www.youtube.com/watch?v=l4PlEaUo7_M 
- **Reference**:https://www.researchgate.net/publication/221206046_A_Simulated_Annealing_Algorithm_for_the_Capacitated_Vehicle_Routing_Problem/link/02e7e533b38058e0da000000/download?_tp=eyJjb250ZXh0Ijp7ImZpcnN0UGFnZSI6InB1YmxpY2F0aW9uIiwicGFnZSI6InB1YmxpY2F0aW9uIn19 

---
