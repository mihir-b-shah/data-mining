import numpy

TOL = 0.00001
a = []

def run(mat,iter):
    mat = numpy.array(mat)
    L = mat
    LT = numpy.matrix.transpose(mat)

    h = numpy.ones(len(mat))
    
    for i in range(0,iter):
        global a
        a = numpy.matmul(LT,h)
        normalize(a)
        h = numpy.matmul(L,a)
        normalize(h)

    print("h: " + str(h))
    print("a: " + str(a))

def change(v0,v1):
    delta = 0
    for i in range(0, len(v0)):
        delta = max(delta, abs(v1[i]-v0[i]))
    global TOL
    return delta <= TOL

def normalize(vect):
    maxnum = 0
    for i in range(0, len(vect)):
        maxnum = max(maxnum, vect[i])

    mult = 1/maxnum
    for i in range(0, len(vect)):
        vect[i] *= mult
    
