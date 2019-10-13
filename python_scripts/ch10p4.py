import math

class point:
    def __init__(self, x0, y0):
        self.x = x0
        self.y = y0

    def __str__(self):
        return str.format("%d %d"%(self.x,self.y))

    def __repr__(self):
        return str.format("%d %d"%(self.x,self.y))

def dist(p1,p2):
    return math.sqrt((p1.x-p2.x)**2+(p1.y-p2.y)**2)

def clust_dist(c1, c2):
    max_dist = 0
    for p1 in c1:
        for p2 in c2:
            max_dist = max(max_dist, dist(p1,p2))
    return max_dist

def aggl_cluster(clusters):
    while(len(clusters) > 1):
        min_dist = 1000
        first = 0
        second = 0
        for i in range(0, len(clusters)-1):
            for j in range(0, i):
                if(clust_dist(clusters[i], clusters[j]) < min_dist):
                    min_dist = clust_dist(clusters[i], clusters[j])
                    first = i
                    second = j
        clusters[second].extend(clusters[first])
        clusters.pop(first)
        print(clusters)

def main():
    clusters = [[point(0,0)], [point(10,10)], [point(5,27)], [point(21,21)],
                [point(28,6)], [point(33,33)]]
    aggl_cluster(clusters)
