package me.leon.ctf

/**
 *
 * @author Leon
 * @since 2022-12-06 8:49
 * @email: deadogone@gmail.com
 */
val DICT_BAI_JIA_XING =
    mapOf(
        '赵' to '0',
        '钱' to '1',
        '孙' to '2',
        '李' to '3',
        '周' to '4',
        '吴' to '5',
        '郑' to '6',
        '王' to '7',
        '冯' to '8',
        '陈' to '9',
        '褚' to 'a',
        '卫' to 'b',
        '蒋' to 'c',
        '沈' to 'd',
        '韩' to 'e',
        '杨' to 'f',
        '朱' to 'g',
        '秦' to 'h',
        '尤' to 'i',
        '许' to 'j',
        '何' to 'k',
        '吕' to 'l',
        '施' to 'm',
        '张' to 'n',
        '孔' to 'o',
        '曹' to 'p',
        '严' to 'q',
        '华' to 'r',
        '金' to 's',
        '魏' to 't',
        '陶' to 'u',
        '姜' to 'v',
        '戚' to 'w',
        '谢' to 'x',
        '邹' to 'y',
        '喻' to 'z',
        '福' to 'A',
        '水' to 'B',
        '窦' to 'C',
        '章' to 'D',
        '云' to 'E',
        '苏' to 'F',
        '潘' to 'G',
        '葛' to 'H',
        '奚' to 'I',
        '范' to 'J',
        '彭' to 'K',
        '郎' to 'L',
        '鲁' to 'M',
        '韦' to 'N',
        '昌' to 'O',
        '马' to 'P',
        '苗' to 'Q',
        '凤' to 'R',
        '花' to 'S',
        '方' to 'T',
        '俞' to 'U',
        '任' to 'V',
        '袁' to 'W',
        '柳' to 'X',
        '唐' to 'Y',
        '罗' to 'Z',
        '薛' to '.',
        '伍' to '-',
        '余' to '_',
        '米' to '+',
        '贝' to '=',
        '姚' to '/',
        '孟' to '?',
        '顾' to '#',
        '尹' to '%',
        '江' to '&',
        '钟' to '*'
    )

val DICT_REVERSE = DICT_BAI_JIA_XING.values.zip(DICT_BAI_JIA_XING.keys).toMap()

fun String.baiJiaXing() = map { DICT_REVERSE[it] ?: it }.joinToString("")

fun String.baiJiaXingDecode() = map { DICT_BAI_JIA_XING[it] ?: it }.joinToString("")
