with open('wordbank.txt') as f:
    wordbank = f.read().splitlines()


while True:
    guess = input("Enter guess >>> ")
    score = input("Enter score (BGYBB) >>> ")

    for i, (letter, color) in enumerate(zip(guess, score)):
        if color == 'B':
            wordbank = [x for x in wordbank if not letter in x]
        if color == 'G':
            wordbank = [x for x in wordbank if x[i] == letter]
        if color == 'Y':
            wordbank = [x for x in wordbank if letter in x and x[i] != letter]
    print(wordbank)
    print(f"Guess: {wordbank[0]}")

