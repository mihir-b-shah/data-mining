import math
import random

def f(x,y):
     return math.exp(-0.5*(y+2*x)**2)/math.sqrt(2*math.pi)

for i in range(-100,100):
     exp = 0
     for j in range(-100,100):
             exp += j*f(i,j)
     exp /= 200
     print("E[Y|X=%d] = %f"%(i,exp))

     
