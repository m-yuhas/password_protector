def dmg = {
    println "foo"
}

def map = [dmg: dmg]
for (arg in args) {
    println arg
    if (arg == "dmg") {
        println "here"
        map[arg]()
    }
}