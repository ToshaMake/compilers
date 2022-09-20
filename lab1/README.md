#Руденко Антон ИВ-922 Вариант 14(Rust)

# №1 Дана грамматика. Постройте вывод заданной цепочки.
## a.
```
S : T | T '+' S | T '-' S;
T : F | F '*' T;
F : 'a' | 'b';
```
Цепочка: ``` a - b * a + b ```
## Решение
```S -> T-S -> F-S -> a-S -> a-T+S -> a-F*T+S -> a-b*T+S -> a-b*a+S -> a-b*a+T -> a-b*a+F -> a-b*a+b```
## b.
```
S : 'a' S B C | 'ab' C;
C B : B C;
'b' B : 'bb';
'b' C : 'bc' ;
'c' C : 'cc';
```
Цепочка: ``` aaabbbccc ```
## Решение
```S -> aSBC -> aaSBCBC -> aaabCBCBC -> aaabBCCBC -> aaabbCCBC -> aaabbCBCC -> aaabbBCCC -> aaabbbCCC -> aaabbbcCC -> aaabbbccC -> aaabbbccc```
# №2 Построить грамматику, порождающую язык:
![Image alt](https://i.imgur.com/phrIhmf.png)
## Решение
## a.
```
G ({a, b, c}, {S, A, B, C}, P, S)
P:
S -> aA
A -> aA | B
B -> bB | C
C -> cC | c
```
## b.
```
G ({0, 10}, {S, A, B}, P, S)
P:
S -> A | B | eps
A -> 0 | 0A | B
B -> 10 | 10B
```
## c.
```
G ({0,1}, {S}, P, S)
P:
S -> 0A0 | 1B1
A -> eps | 00 | 0A0 | B
B -> eps | 11 | 1B1 | A
```
# №3 К какому типу по Хомскому относится грамматика с приведенными правилами? Аргументируйте ответ.
## a.
```
S : '0' A '1' | '01';
'0' A : '00' A '1';
A : '01'; 
```
### Фразовые: Подходят все - ДА 
### Контекстно зависимые: '0' A : '00' A '1'; присутствует контекст (Терминальный символ 0) -> удовлетворяет условию (α1Аα2 -> α1βα2 где α1 , α2 ∈ V* , А ∈ VN, β ∈ V+) -> Да 
### Контекстно свободные:  '0' A : '00' A '1'; присутствует контекст (Терминальный символ 0) -> не удовлетворяет условию (А -> β, где А ∈ VN, β ∈ V+ или β ∈ V*) -> Нет
### Регулярные: Не является контекстно свободной -> Нет
## Ответ: КЗ грамматика.
## b.
```
S : A 'b';
A : A 'a' | 'ba';
```
### Фразовые: Подходят все - ДА 
### Контекстно зависимые: присутствует 0 контекст (α1 и α2 равны eps) -> удовлетворяют условию (α1Аα2 -> α1βα2 где α1 , α2 ∈ V* , А ∈ VN, β ∈ V+) -> Да 
### Контекстно свободные: удовлетворяет условию (А -> β, где А ∈ VN, β ∈ V+ или β ∈ V*) -> Да
### Регулярные: удовлетворяет условию (В левой части терминальный символ в правой части не более 1 не терминальный символ) А -> Bγ | yB или А -> γ, где А, В ∈ VN, γ ∈ VТ* -> Да
## Ответ: Регулярная грамматика.
# №4 Построить КС-грамматику, эквивалентную грамматике с правилами:
```
S : A B | A B S;
A B : B A;
B A : A B;
A : 'a';
B : 'b';
```
## Решение
```
S: A | B;
A: 'ab' C;
B: 'ba' C;
C: S | eps;
```
# №5 Построить регулярную грамматику, эквивалентную грамматике с правилами:
```
S : A '.' A;
A : B | B A;
B : '0' | '1';
```
## Решение
```
S: A | B;
A: '1' A | '1' B | '1' C;
B: '0' B | '0' A | '0' C;
C: '.' D | '.' E;
D: '1' D | '1' E | 1;
E: '0' E | '0' D | 0;
```
# №6 Напишите регулярное выражение для:
## a. множества идентификаторов, где идентификатор – это последовательность букв или цифр, начинающаяся с буквы или _;
### 1. ^[a-zA-Z_]\w*
### 2. [regexr.com](https://regexr.com/6gnqd)

## b. множества вещественных констант с плавающей точкой, состоящих из целой части, десятичной точки, дробной части, символа е или Е, целого показателя степени с необязательным знаком и необязательного суффикса типа – одной из букв f, F, l или L. Целая и дробная части состоят из последовательностей цифр. Может отсутствовать либо целая, либо дробная часть (но не обе сразу).
### 1. (\b[\d]+[.]|[.]\d{1})[\d]*([eE][+-]?[\d]+)?[fFlL]?
### 2. [regexr.com](https://regexr.com/6havt)
# №7 Для регулярных выражений из предыдущего задания постройте конечные автоматы. Изобразите их в виде графа.
## Решение
## a. ![Image alt](https://g.gravizo.com/svg?digraph%20G%20%7B%20size%20=%224,4%22;%20begin%20[shape=box];%20end%20[shape=box];%20begin%20-%3E%20id%20[style=bold,label=%22[a-zA-Z_]%22];%20id%20-%3E%20id%20[style=bold,label=%22[%5C%5Cw]%22];%20id%20-%3E%20end%20[style=bold,label=%22[⊥]%22];%20%7D)
<details> 
<summary></summary>
  digraph G {
    size ="4,4";
    begin [shape=box];
    end [shape=box];
    begin -> id [style=bold,label="[a-zA-Z_]"];
    id -> id [style=bold,label="[\\w]"];
    id -> end [style=bold,label="[⊥]"]; 
  }
</details>

## b. ![Image alt](https://g.gravizo.com/svg?digraph%20G%20%7B%20size%20=%224,4%22;%20begin%20[shape=box];%20end%20[shape=box];%20begin%20-%3E%20int%20[style=bold,label=%22[%5C%5Cd]%22];%20int%20-%3E%20int%20[style=bold,label=%22[%5C%5Cd]%22];%20int%20-%3E%20real%20[style=bold,label=%22[.]%22];%20begin%20-%3E%20dot%20[style=bold,label=%22[.]%22];%20dot%20-%3E%20real%20[style=bold,label=%22[%5C%5Cd]%22];%20real%20-%3E%20real%20[style=bold,label=%22[%5C%5Cd]%22];%20real%20-%3E%20realE%20[style=bold,label=%22[eE]%22];%20realE%20-%3E%20realEsign%20[style=bold,label=%22[+-]%22];%20realEsign%20-%3E%20realdegree%20[style=bold,label=%22[%5C%5Cd]%22];%20realE%20-%3E%20realdegree%20[style=bold,label=%22[%5C%5Cd]%22];%20realdegree%20-%3E%20realdegree%20[style=bold,label=%22[%5C%5Cd]%22];%20realdegree%20-%3E%20realdegreesuff%20[style=bold,label=%22[fFlL]%22];%20realdegreesuff%20-%3E%20end%20[style=bold,label=%22[⊥]%22];%20realdegree%20-%3E%20end%20[style=bold,label=%22[⊥]%22];%20real%20-%3E%20realdegreesuff%20[style=bold,label=%22[fFlL]%22];%20real%20-%3E%20end%20[style=bold,label=%22[⊥]%22];%20%7D)
<details> 
<summary></summary>
  digraph G {
    size ="4,4";
    begin [shape=box];
    end [shape=box];
    begin -> int [style=bold,label="[\\d]"];
    int -> int [style=bold,label="[\\d]"];
    int -> real [style=bold,label="[.]"];
    begin -> dot [style=bold,label="[.]"];
    dot -> real [style=bold,label="[\\d]"];
    real -> real [style=bold,label="[\\d]"];
    real -> realE [style=bold,label="[eE]"];
    realE -> realEsign [style=bold,label="[+-]"];
    realEsign -> realdegree [style=bold,label="[\\d]"];
    realE -> realdegree [style=bold,label="[\\d]"];
    realdegree -> realdegree [style=bold,label="[\\d]"];
    realdegree -> realdegreesuff [style=bold,label="[fFlL]"];
    realdegreesuff -> end [style=bold,label="[⊥]"]; 
    realdegree -> end [style=bold,label="[⊥]"];
    real -> realdegreesuff [style=bold,label="[fFlL]"];
    real -> end [style=bold,label="[⊥]"]; 
  }
</details>







