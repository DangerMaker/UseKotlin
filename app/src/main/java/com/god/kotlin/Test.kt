package com.god.kotlin
//
//fun <T, R> Collection<T>.fold(
//    initial: R,
//    combine: (acc: R, nextElement: T) -> R
//): R {
//    var accumulator: R = initial
//    for (element: T in this) {
//        accumulator = combine(accumulator, element)
//    }
//    return accumulator
//}

val items = listOf(1, 2, 3, 4, 5)

fun main(){
//    items.fold(0, {
//        // 如果一个 lambda 表达式有参数，前面是参数，后跟“->”
//            acc: Int, i: Int ->
//        print("acc = $acc, i = $i, ")
//        val result = acc + i
//        println("result = $result")
//        // lambda 表达式中的最后一个表达式是返回值：
//        result
//    })
//
//    val joinedToString = items.fold("Elements:", { acc, i -> acc + " " + i })
//    println(joinedToString)
//
//    val product = items.fold(1, Int::times)
//    println(product)

    val sum = { x: Int, y: Int -> x + y }
    println(sum(1,1)
    )

}

