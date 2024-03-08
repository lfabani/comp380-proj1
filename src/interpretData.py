#Here is where you can make the python script Paul!
import matplotlib.pyplot as plt
import numpy as np
import csv

xlist = []
ylist = []

choice = input("Choose which graph to make (1 = Dynamic Alpha. 2 = Dynamic Theta, 3 = Both): ")

if choice == '1':
    with open('../comp380-proj1/dataForStats/CSVS/DynamicAlpha.csv', mode = 'r') as file:
        csvFile = csv.reader(file)
        for line in csvFile:
            xlist.append(float(line[2]))
            ylist.append(float(line[1]) * 100)
        print(xlist)
        print(ylist)
        xpoints = np.array(xlist)
        ypoints = np.array(ylist)

        plt.plot(xpoints, ypoints, 'o')
        plt.xlabel("Alpha")
        plt.ylabel("% Accuracy")
        plt.show()

if choice == '2':
    with open('../comp380-proj1/dataForStats/CSVS/DynamicTheta.csv', mode = 'r') as file:
        csvFile = csv.reader(file)
        for line in csvFile:
            xlist.append(float(line[0]))
            ylist.append(float(line[1]) * 100)
        print(xlist)
        print(ylist)
        xpoints = np.array(xlist)
        ypoints = np.array(ylist)

        plt.plot(xpoints, ypoints, 'o')
        plt.xlabel("Theta")
        plt.ylabel("% Accuracy")
        plt.show()

if choice == '3':
    with open('../comp380-proj1/dataForStats/CSVS/DynamicAlphaAndTheta.csv', mode = 'r') as file:
        csvFile = csv.reader(file)
        for line in csvFile:
            xlist.append(float(line[2]))
            ylist.append(float(line[1]) * 100)
        print(xlist)
        print(ylist)
        xpoints = np.array(xlist)
        ypoints = np.array(ylist)

        plt.plot(xpoints, ypoints, 'o')
        plt.xlabel("Alpha")
        plt.ylabel("% Accuracy")
        plt.show()