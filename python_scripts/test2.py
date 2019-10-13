import random as r
import math
import numpy as np
from matplotlib import pyplot as plt
import scipy.optimize as opt

def exp(t,a):
    return a*np.exp(t)+1

h_vals = []
for i in range(0,1000000):
    h_vals.append(math.log(r.random()*(r.random()+2)))
h_vals.sort()

z_vals = []
h_gph = []
comp_gph = []
my_gph = []

h_ptr = 0
# from -8 to 2

for i in range(-8000,1000):
    z = i/1000
    z_vals.append(z)
    while(h_ptr < len(h_vals) and h_vals[h_ptr] < z):
        h_ptr+=1
    h_gph.append(1-h_ptr/1000000)


plt.plot(z_vals,h_gph)
opt_arr = opt.curve_fit(exp,z_vals,h_gph,p0=[-0.333])
print(opt_arr)

for i in range(-8000,1000):
    z = i/1000
    v = exp(z,opt_arr[0][0])
    comp_gph.append(v)
    my_gph.append(1-np.log(1.5)*np.exp(z))

plt.plot(z_vals,my_gph)
plt.show()
    

