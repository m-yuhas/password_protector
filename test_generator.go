package main

import (
  "fmt"
  "password_protector/password_generator"
)

func main() {
  bytes := []byte{'1', '2', 'a', 'b', 'c'}
  fmt.Println(password_generator.GeneratePassword(4, bytes))
}
