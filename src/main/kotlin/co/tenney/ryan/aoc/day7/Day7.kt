package co.tenney.ryan.aoc.day7

import co.tenney.ryan.aoc.AocDay

class Day7 : AocDay<Int>(7) {

    val allDirectories: MutableList<Directory> = mutableListOf<Directory>()
    val rootDir: Directory = Directory("/")

    override fun processInput(input: List<String>): Unit {
        var cwd: Directory = rootDir
        val path: ArrayDeque<Directory> = ArrayDeque<Directory>()
        var ls = false
        input.forEach({ cmd ->
            if (ls) {
                if (cmd.startsWith("$")) {
                    ls = false
                } else {
                    val parts = cmd.split(" ")
                    if (parts[0] == "dir") {
                        val newDir = cwd.addDirectory(parts[1])
                        allDirectories.add(newDir)
                    } else {
                        cwd.addFile(parts[1], parts[0].toInt())
                    }
                }
            }

            if (cmd == "$ ls") {
                ls = true
            } else if (cmd.startsWith("$ cd ")) {
                val cd = cmd.substring(5)
                when (cd) {
                    "/" -> {
                        path.clear()
                        cwd = rootDir
                    }
                    ".." -> {
                        cwd = path.removeLast()
                    }
                    else -> {
                        path.add(cwd)
                        cwd = cwd.getDirectory(cd)!!
                    }
                }
            }
        })
    }

    override fun part1(): Int {
        return allDirectories.map(Directory::size).filter({ it < 100_000 }).sum()
    }

    override fun part2(): Int {
        val totalSize = 70000000
        val desiredFree = 30000000
        val targetSize = totalSize - desiredFree
        val currentSize = rootDir.size()
        val needToFree = currentSize - targetSize
        return allDirectories.map(Directory::size).filter({ it > needToFree }).min()
    }

}

data class File(val name: String, val size: Int)

data class Directory(val name: String) {

    val files: MutableList<File> = mutableListOf<File>()
    val directories: MutableMap<String, Directory> = mutableMapOf<String, Directory>()

    fun size(): Int {
        return files.sumOf(File::size) + directories.values.sumOf(Directory::size)
    }

    fun addFile(name: String, size: Int): File {
        val file = File(name, size)
        files.add(file)
        return file
    } 

    fun addDirectory(name: String): Directory {
        if (name in directories) {
            return directories.get(name)!!
        }
        val directory = Directory(name)
        directories.put(name, directory)
        return directory
    } 

    fun getDirectory(name: String): Directory? {
        return directories.get(name)
    } 

}
