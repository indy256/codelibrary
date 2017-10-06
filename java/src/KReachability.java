/* An integer is said to be k-reachable from another integer if the latter can be converted 
   to the former by replacing at most k digits. Note that leading 0's are not considered the 
   part of the number and hence are not allowed to replaced with non-zero integers.

For example,

1123 is 15-reachable from 2223 but not 1-reachable.

6 is 2-reachable from 10, but 10 is not 2-reachable from 6

Mancunian has been given the following problem. He is provided the initial integer 
N
N and the value of 
k
k. Now he has to answer 
Q
Q queries. Each query is represented by a pair (L, R). The answer for a query is the number of integers in the range 
L
L to 
R
R (inclusive) which are k-reachable from 
N
N.
