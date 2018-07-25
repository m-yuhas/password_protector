package main

import "fmt"

type thinger interface{
  foo( str string )
}

type thing struct{
  str string
  mp map[string]string
}

func (t *thing) foo( str string ) {
  t.str = str
}

func main() {
  a := thing{}
  a.foo("blah")
  fmt.Println(a)
}
