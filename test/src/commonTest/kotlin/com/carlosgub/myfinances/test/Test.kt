import kotlin.test.Test

/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
class GAAA {
    @Test
    fun main() {
        println(isIsomorphic("egg", "add"))
        println(isIsomorphic("foo", "bar"))
        println(isIsomorphic("cola", "hola"))

    }

    fun isIsomorphic(a: String, b: String): Boolean {
        if (a.length != b.length) return false
        val map = mutableMapOf<Char, Char>()
        for (i in 1..<a.length) {
            val s = a[i]
            if (map.containsKey(s)){
                if(b[i] != map[s]) return false
            }else{
                map[s] = b[i]
            }
        }
        return true
    }
}

