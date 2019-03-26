package password_generator

import (
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
        if len(password) > 0 && uint(len(password)) != input.length {
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
