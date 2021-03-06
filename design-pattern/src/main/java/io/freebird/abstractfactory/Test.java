package io.freebird.abstractfactory;

/**
 * @author freebird
 */
public class Test {

    public static void main(String[] args) {
        AbstractHumanFactory factory = new ChineseHumanFactory();
        Human human = factory.createBritishHuman();
        Human chineseHuman = factory.createChineseHuman();
    }
}
