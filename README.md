# programming.language

# Язык программирования


## Служебные слова
**var** : Объявление переменной

**if** : Условный оператор

**while** : Цикл без счетчика

**List** : Список

**print(oject)** : Вывод значения с табуляцией

**print()** : Вывод символа табуляции

**println(object)** : Вывод значения с переносом строки

**println()** : Вывод символа переноса строки

**true** : Логическая истина

**false** : Логическая ложь


## Пример
```
Пример работы языка:
- заполненеие списка несколькими элементами
- вывод этих эелментов
- сортировка
- вывод отсортированного списка
- создание 2-х списков и заполнение их по принципу кратности 2-м
- вывод размера и элементов каждого из полученных списков
```

```php
var list # List

list::addBackward(14)
list::addBackward(34)
list::addBackward(76)
list::addBackward(8)
list::addBackward(90)
list::addBackward(13)

var iterator = 0
while iterator < list::getSize:
    print list::get(iterator)
    iterator = iterator + 1
println

var i = list::getSize() - 1
while i > 0 :
    var j = 0
    while j < i :
        if list::get(j) > list::get(j + 1) :
            var bufferElement = list::get(j)
            list::set(list::get(j+1), j)
            list::set(bufferElement, j + 1)
        j = j + 1
    i = i - 1

iterator = 0
while iterator < list::getSize:
    print list::get(iterator)
    iterator = iterator + 1
println

var listPositiveMultiplicity # List
var listNegativeMultiplicity # List

i = 0
while i < list::getSize:
    var bufferElement = list::get(i)

    if bufferElement % 2 == 0 :
        listPositiveMultiplicity::addBackward(bufferElement)
    else :
        listNegativeMultiplicity::addBackward(bufferElement)

    i = i + 1

println listPositiveMultiplicity::getSize()
iterator = 0
while iterator < listPositiveMultiplicity::getSize:
    print listPositiveMultiplicity::get(iterator)
    iterator = iterator + 1
println

println listNegativeMultiplicity::getSize()
iterator = 0
while iterator < listNegativeMultiplicity::getSize:
    print listNegativeMultiplicity::get(iterator)
    iterator = iterator + 1
println
```

```
14	34	76	8	90	13	

8	13	14	34	76	90	

5
8	14	34	76	90	

1
13
