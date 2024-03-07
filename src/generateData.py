import random


characters = {
    ("A","1 -1 -1 -1 -1 -1 -1") : [[-1, -1,  1,  1, -1, -1, -1],
 [-1, -1, -1,  1, -1, -1, -1],
 [-1, -1, -1,  1, -1, -1, -1],
 [-1, -1,  1, -1,  1, -1, -1],
 [-1, -1,  1, -1,  1, -1, -1],
 [-1,  1,  1,  1,  1,  1, -1],
 [-1,  1, -1, -1, -1,  1, -1],
 [-1,  1, -1, -1, -1,  1, -1],
 [ 1,  1,  1, -1,  1,  1,  1]],
 ("B","-1 1 -1 -1 -1 -1 -1") : [[1, 1, 1, 1, 1, 1, -1],
 [-1, 1, -1, -1, -1, -1, 1],
 [-1, 1, -1, -1, -1, -1, 1],
 [-1, 1, -1, -1, -1, -1, 1],
 [-1, 1, 1, 1, 1, 1, -1],
 [-1, 1, -1, -1, -1, -1, 1],
 [-1, 1, -1, -1, -1, -1, 1],
 [-1, 1, -1, -1, -1, -1, 1],
 [1, 1, 1, 1, 1, 1, -1]],
 ("C","-1 -1 1 -1 -1 -1 -1") : [[-1, -1, 1, 1, 1, 1, 1],
 [-1, 1, -1, -1, -1, -1, 1],
 [1, -1, -1, -1, -1, -1, -1],
 [1, -1, -1, -1, -1, -1, -1],
 [1, -1, -1, -1, -1, -1, -1],
 [1, -1, -1, -1, -1, -1, -1],
 [1, -1, -1, -1, -1, -1, -1],
 [-1, 1, -1, -1, -1, -1, 1],
 [-1, -1, 1, 1, 1, 1, -1]],
 ("D","-1 -1 -1 1 -1 -1 -1") : [[1, 1, 1, 1, 1, -1, -1],
 [-1, 1, -1, -1, -1, 1, -1],
 [-1, 1, -1, -1, -1, -1, 1],
 [-1, 1, -1, -1, -1, -1, 1],
 [-1, 1, -1, -1, -1, -1, 1],
 [-1, 1, -1, -1, -1, -1, 1],
 [-1, 1, -1, -1, -1, -1, 1],
 [-1, 1, -1, -1, -1, 1, -1],
 [1, 1, 1, 1, 1, -1, -1]],
 ("E","-1 -1 -1 -1 1 -1 -1") : [[1, 1, 1, 1, 1, 1, 1],
 [-1, 1, -1, -1, -1, -1, 1],
 [-1, 1, -1, -1, -1, -1, -1],
 [-1, 1, -1, 1, -1, -1, -1],
 [-1, 1, 1, 1, -1, -1, -1],
 [-1, 1, -1, 1, -1, -1, -1],
 [-1, 1, -1, -1, -1, -1, -1],
 [-1, 1, -1, -1, -1, -1, 1],
 [1, 1, 1, 1, 1, 1, 1]],
 ("J","-1 -1 -1 -1 -1 1 -1") : [[-1, -1, -1, 1, 1, 1, 1],
 [-1, -1, -1, -1, -1, 1, -1],
 [-1, -1, -1, -1, -1, 1, -1],
 [-1, -1, -1, -1, -1, 1, -1],
 [-1, -1, -1, -1, -1, 1, -1],
 [-1, -1, -1, -1, -1, 1, -1],
 [-1, 1, -1, -1, -1, 1, -1],
 [-1, 1, -1, -1, -1, 1, -1],
 [-1, -1, 1, 1, 1, -1, -1]],
 ("K","-1 -1 -1 -1 -1 -1 1") : [[1, 1, 1, -1, -1, 1, 1],
 [-1, 1, -1, -1, 1, -1, -1],
 [-1, 1, -1, 1, -1, -1, -1],
 [-1, 1, 1, -1, -1, -1, -1],
 [-1, 1, 1, -1, -1, -1, -1],
 [-1, 1, -1, 1, -1, -1, -1],
 [-1, 1, -1, -1, 1, -1, -1],
 [-1, 1, -1, -1, -1, 1, -1],
 [1, 1, 1, -1, -1, 1, 1]]

}

def generate_data_with_noise_to_file(characters, noise, filename):
    with open(filename, 'w') as file:
        file.write("67\n7\n1050\n\n")
        for i in range(150):    
            for char, pattern in characters.items():
                
                for row in pattern:
                    noisy_row = []
                    for elem in row:
                        # Add noise to each element based on the noise parameter
                        noisy_elem = -elem if random.random() < noise else elem
                        noisy_row.append(noisy_elem)
                    string_list = [str(element) for element in noisy_row]

                    # Join elements with commas
                    csv_string = ' '.join(string_list)
                    file.write(csv_string+"\n")
                
                file.write('\n')
                file.write(char[1] + '\n')
                file.write(char[0] + '\n')
                file.write('\n')

generate_data_with_noise_to_file(characters,0.2,"0pt2NoiseTraining.txt")