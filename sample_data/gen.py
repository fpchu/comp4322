import random

def main(maxNodeNum):
    num_of_nodes = random.randint(5, maxNodeNum)
    nodes = set()
    for i in range(num_of_nodes):
        node_id = formulate_id()
        while node_id in nodes:
            node_id = formulate_id()
        nodes.add(node_id)




def formulate_id():
    node_id = ""
    characters = "abcdefghijklmnopqrstuvwxyz".upper()
    id_num_of_Char = random.randint(1, 10)
    for i in range(id_num_of_Char):
        node_id += characters[random.randint(1, len(characters)]
    return node_id

if __name__ == '__main__':
    maxNodeNum = 1000
    main(maxNodeNum)
