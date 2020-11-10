import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Main {

    public static List<Character> VnList = new ArrayList<>();
    public static List<Character> VtList = new ArrayList<>();
    public static List<String> listRule = new ArrayList<>();
    public static List<FirstVt> firstVt = new ArrayList<>();
    public static List<LastVt> lastVt = new ArrayList<>();
    public static List<Rule> ruleList = new ArrayList<>();
    public static List<Vt> vtList = new ArrayList<>();
    public static Stack<Character> S = new Stack<>();

    public static String stack = "#";
    public static int[][] a = {
            {0,-1,-1,-1,-1,-1},
            {0,0,0,1,1,1},
            {0,-1,-1,2,-1,-1},
            {0,0,0,1,1,1},
            {0,-1,-1,1,1,-1},
            {0,-1,-1,1,1,1}
    };

    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader(args[0]);
        init();
        insertV();
        insertRules();
//        test();
         int ch = 0;
         String s = new String();
        while( ( ch = fileReader.read()) != -1)
        {
            char c = (char)ch;
            s+=c;
            if(c == '\r' || c == '\n')
                break;
            if(!fun(c)) {
                return;
            }
        }
        if(!fun('#')) {
            return;
        }
        while(statute()){
        }
        if(!"#N".equals(stack))
            System.out.println("RE");

    }

    public static boolean fun(char c){
        int result = cmp(c);
        if(result == 0){
            System.out.println("E");
            return false;
        }
        else if(result == -1 || result == 2){
            System.out.println("I" + c);
            stack = stack + c;
        }
        else {
            if (!statute()) {
                System.out.println("RE");
                return false;
            }
            return fun(c);
        }
        return true;
    }

    public static boolean statute(){
        for(int i = stack.length() -1 ; i >= 0 ;i --){
            for(int j = i ; j >= 1;j--){
                String s = stack.substring(stack.length() - j,stack.length());
                for(Rule rule : ruleList){
                    if(rule.rule.equals(s)){
                        if(s.equals("i") || s.equals("N+N") || s.equals("N*N") || s.equals("(N)"))
                            System.out.println("R");
                        stack = stack.substring(0,stack.length() - j) + "N";
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int cmp(char c){
        if(!VtList.contains(c))
            return 0;
        char vt = getLastVt();
//        if(vt == '#')
//            return -1;
        Vt up = new Vt();
        Vt in = new Vt();
        for(Vt k : vtList){
            if(k.vt == c)
                in = k;
            if(k.vt == vt)
                 up = k;
        }
//        if(up.i == 0)
//            return 0;
        return a[up.i][in.i];
    }

    public static char getLastVt(){
        for(int i = stack.length()-1; i>=0 ; i --){
            if(VtList.contains(stack.charAt(i)))
                return stack.charAt(i);
        }
        return '#';
    }

    public static void init(){
        S.add('#');
        vtList.add(new Vt('#',0));
        vtList.add(new Vt('i',1));
        vtList.add(new Vt('(',2));
        vtList.add(new Vt(')',3));
        vtList.add(new Vt('+',4));
        vtList.add(new Vt('*',5));
    }

    public static void getFirstVtAndLAstVt(){
        FirstVt FE = new FirstVt();
        FE.Vn = 'E';
        FE.list.add('+');
        FE.list.add('(');
        FE.list.add('i');
        FE.list.add('*');

        FirstVt FT = new FirstVt();
        FT.Vn = 'T';
        FT.list.add('(');
        FT.list.add('*');
        FT.list.add('i');

        FirstVt FF = new FirstVt();
        FF.Vn = 'F';
    }

    public static void insertRules(){
        String[] rules = {
                "N=>N+N|N*N|(N)|i"
        };
        for(String rule : rules)
            analyzeRule(rule);
        Collections.addAll(listRule, rules);
    }

    public static void test(){
        System.out.println(VtList);
        System.out.println(VnList);
        System.out.println(listRule);
        for(Rule rule:ruleList){
            System.out.println(rule.Vn + "=>" + rule.rule);
        }
        System.out.println(firstVt);
        System.out.println(lastVt);
    }


    public static void insertV(){
        VnList.add('E');
        VnList.add('T');
        VnList.add('F');

        VtList.add('+');
        VtList.add('*');
        VtList.add('(');
        VtList.add(')');
        VtList.add('i');
    }

    public static void analyzeRule(String rule){
        String string[] = rule.split("=>");
//        System.out.println(string[1].toString());
        char vt = string[0].charAt(0);
        String[] rules = string[1].split("\\|");
//        System.out.println(rules[0].toString());
        for(String r: rules){
            ruleList.add(new Rule(vt,r));
        }
    }

//    public static boolean insertLastVt(Character Vn,Character Vt){
//        return insertVnAndVt(Vn, Vt, lastVt);
//    }

    private static boolean insertVnAndVt(Character Vn, Character Vt, List<V> lastVt) {
        if(!VnList.contains(Vn) || !VtList.contains(Vt))
            return false;
        for(V v : lastVt){
            if(v.Vn == Vn){
                if(!v.listVt.contains(Vt)) {
                    v.listVt.add(Vt);
                }
                return true;
            }
        }
        V v = new V(Vt,Vn);
        lastVt.add(v);
        return true;
    }
//
//    public static boolean insertFirstVt(Character Vn,Character Vt){
//        return insertVnAndVt(Vn, Vt, (List<V>) firstVt);
//    }
}

class Vt{
    char vt;
    int i;
    public Vt(char c,int i){
        this.i = i;
        vt = c;
    }
    public Vt(){};
}

class FirstVt{
    public char Vn;
    public List<Character> list = new ArrayList<>();
}

class LastVt{
    public char Vn;
    public List<Character> list = new ArrayList<>();
}

class Rule{
    public char Vn;
    public List<Character> v = new ArrayList<>();
    public String rule;
    public Rule(char Vn,String rule){
        this.Vn = Vn;
        this.rule = rule;
        for(int i = 0; i < rule.length();i++){
            v.add(rule.charAt(i));
        }
    }
}

class V{
    public char Vn;
    public List<Character> listVt = new ArrayList<>();
    public V(){}
    public V(char a,char b){
        Vn = a;
        listVt.add(b);
    }
    public V(Character a,Character b){
        Vn = a;
        listVt.add(b);
    }
}
