# Python Script to run the different experiments in bash

import subprocess

THREADS = [i for i in range(1, 21)]
SET_NUMBERS = [100000, 200000, 500000, 1000000]\
    + [i * 2000000 for i in range(1, 5)]

JAVA = "java"
FLAG = "-cp"
DIR = "bin"
SERIAL = "Serial"
THREADED = "ThreadedApp"
FORKED = "ForkedApp"


def runSerials():
    print("Running Serials")
    for size in SET_NUMBERS:
        print(size)
        for i in range(3):
            subprocess.run(
                [JAVA, FLAG, DIR, SERIAL, str(size)],
            )


def runThreaded():
    for size in SET_NUMBERS:
        print(size)
        for no in THREADS:
            for i in range(3):
                subprocess.run(
                    [JAVA, FLAG, DIR, THREADED, str(size), str(no)],
                )


def runForked():
    for size in SET_NUMBERS:
        print(size)
        for no in THREADS:
            for i in range(3):
                subprocess.run(
                    [JAVA, FLAG, DIR, FORKED, str(size), str(no)],
                )


def main():
    print("About to run the tests!")
    print("")
    print("Running the serials")
    runSerials()
    print()
    print("Running Threaded")
    runThreaded()
    print()
    print("Running Forked")
    runForked()
    print()


main()
