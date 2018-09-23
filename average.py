def main():
    with open("output.txt", "r+") as f:
        lines = f.readlines()

    for i in range(0, len(lines), 3):
        line1 = lines[i].strip().split(",")
        line2 = lines[i+1].strip().split(",")
        line3 = lines[i+2].strip().split(",")
        average = (eval(line1[1]) + eval(line2[1]) + eval(line3[1])) / 3
        average_time = (eval(line1[2]) + eval(line2[2]) + eval(line3[2])) / 3
        data_set = eval(line1[0])
        if len(line1) == 3:
            print("{},{},{}".format(data_set, average, average_time))
        else:
            print("{},{},{},{}".format(
                data_set, average, average_time, line1[3])
            )


main()
