# Euler 110
 UFO (short for UnitFractionOperator) files are an efficient solver for [Euler Project Problem 110](https://projecteuler.net/problem=110) for arbitrary inputs, including those far beyond the 4 million of the original problem. The results of this code it finds are listed in the OEIS as a [record sequence](https://oeis.org/A126098) of the  [sequence counting the number of solutions](https://oeis.org/A018892).  The UFO has been checked against these sequence.

## How it works:

It can be shown that there is a one to one mapping from every pair of coprime factors of **n** and a corresponding pair of denominators, **x** and **y**.  Every coprime pair of this form also yields one factor of **n**<sup>2</sup>, and so the number of solutions is equal to the number of factors of **n**<sup>2</sup>. This in turn is dependent on the prime signature of **n**, where minimums must be that smallest value of that prime signature (i.e a subset of [this sequence](https://oeis.org/A025487)).

This algorithm checks candidates by storing an int array which represents the current number's prime signature, rather than iterating through numbers themselves. The array is added to in the two's place, then "rolls over" (much like a numerical base) when the value it represents passes the current accepted minimum. Because the number of primes is upper-bounded by log<sub>3</sub>(solutions), the algorithm can terminate there.

This pattern greatly reduces the number of candidates, and makes it possible to test only  good values, even for large inputs.
## UFO vs UFO2:
I was able to experimentally find a range in which values occur frequently, and developed an approximation which is very accurate, especially for large values: 
By restricting the testing to the indices around this approximate value, the algorithm can be made to run with functionally equivalent accuracy and handle inputs of *much* larger sizes ( e.g. The first denominator with a googol solutions is approx. **108676 x 10<sup>538</sup>**, and computes in roughly 3 seconds). 

This  optimized version of the algorithm is included in the UFO.java file , while a slower version with more prime coverage is included in the UFO2.java. In theory, if the faster version misses a solutions, this version, while slower, would catch it. It is worth noting that as of yet, the faster version has not missed a solution, when compared with the wider sweep. 
## Trade-Offs:
The prime generator used is rather slow, but it can generate any number of primes, with relatively low memory usage. Since the number of primes needed is logarithmic, neither speed nor memory are pressing concerns, but I chose this implementation over a lookup table or sieve method because I liked the way it generated primes in an expandable way. 

The input for the UFO's is a Java double, which means it loses precision at large input sizes like the ones handled. This was designed with the knowlege that increasingly large intervals of numbers give the same answer, and the likelyhood of being close enough to a boundary for precision loss to affect the correctness of the answer is small. This could be easily changed using a library like BigDecimal, but for the sake of easy and readable code, I decided a double was sufficient. 

Both UFO and UFO2 files are included as an acknowledgement that although it hasn't yet, UFO could theoretically fail some inputs. UFO is markedly faster than UFO2, especially at larger input sizes.
## Usage:
after compilation, UFO and UFO2 run from the command line, with the desired number of solutions as the only argument. They will print the number to the command line, as well as display information about the algorithm, such as runtime, number of primes used, estimated number of primes used, actual number of solutions, etc.