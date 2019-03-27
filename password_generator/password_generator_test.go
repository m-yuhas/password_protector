package password_generator

import (
    "math"
    "testing"
)

func TestGeneratePasswordOneList(t *testing.T) {
    inputData := []struct {
        length uint
        characterList1 []byte
    }{
        {
            0,
            []byte{'a'},
        },
        {
            1,
            []byte{'a', 'b'},
        },
        {
            2,
            []byte{'a', 'b', 'c'},
        },
        {
            3,
            []byte{'a', 'b', 'c', 'd'},
        },
        {
            100,
            []byte{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l'},
        },
        {
            4,
            []byte{'\x00', '\x03', '\xff'},
        },
    }
    for _, input := range inputData {
        password, err := GeneratePassword(input.length, input.characterList1)
        if err != nil {
            t.Errorf("An error occurred for input %s.", input)
        }
        if uint(len(password)) != input.length {
            t.Errorf(
                "Length is %d, not %d, for input %s.",
                len(password),
                input.length,
                input,
            )
        }
        for _, char := range password {
            found := false
            for _, c := range input.characterList1 {
                if char == c {
                    found = true
                }
            }
            if !found {
                t.Errorf(
                    "Character %c found in result, but not in input %s.",
                    char,
                    input,
                )
            }
        }
    }
}

func TestGeneratePasswordMultipleLists(t *testing.T) {
    length := uint(1000)
    password, err := GeneratePassword(length, []byte{'A', 'B'}, []byte{'B'})
    if err != nil {
        t.Errorf("An error occurred for 2-slice input.")
    }
    if uint(len(password)) != length {
        t.Errorf(
            "Length for 2-slice input is incorrect: %d should be %d.",
            len(password),
            length,
        )
    }
    for _, char := range password {
        found := false
        for _, c := range []byte{'A', 'B'} {
            if char == c {
                found = true
            }
        }
        if !found {
            t.Errorf("Character %c found in 2-slice input password.", char)
        }
    }
    password, err = GeneratePassword(length, []byte{'A', 'B'}, []byte{})
    if err != nil {
        t.Errorf("An error occurred for 2-slice input, one slice nil")
    }
    if uint(len(password)) != length {
        t.Errorf(
            "Length for 2-slice input, one slice nil is incorrect: %d.",
            len(password),
        )
    }
    for _, char := range password {
        found := false
        for _, c := range []byte{'A', 'B'} {
            if char == c {
                found = true
            }
        }
        if !found {
            t.Errorf(
                "Character %c found in 2-slice input, on slice nil password.",
                char,
            )
        }
    }
}

func TestGeneratePasswordNilInput(t *testing.T) {
    _, err := GeneratePassword(100, []byte{})
    if err == nil {
        t.Errorf("Expected error for nil byte array input, but found none.")
    }
}

func TestGeneratedPasswordCharacterFrequency(t *testing.T) {
    passwordSize := uint(100000)
    allowableDeviation := 0.01
    allowableCharacters := []byte{'A', 'B', 'C', '1', '2', '3'}
    password, err := GeneratePassword(passwordSize, allowableCharacters)
    if err != nil {
        t.Errorf("Did not expect error during character frequency test.")
    }
    characterCounts := map[byte]uint{}
    for _, char := range allowableCharacters {
        characterCounts[char] = 0
    }
    for _, char := range password {
        if _, ok := characterCounts[char]; ok {
            characterCounts[char]++
        } else {
            t.Errorf(
                "Did not expect character %c for input %s.",
                char,
                allowableCharacters,
            )
        }
    }
    for char, count := range characterCounts {
        totalDeviation := math.Abs(
            1 / float64(len(allowableCharacters)) -
            float64(count) / float64(passwordSize),
        )
        if totalDeviation > allowableDeviation {
            t.Errorf(
                "Found deviation of %f for frequncy of character %c.",
                totalDeviation,
                char,
            )
        }
    }
}
