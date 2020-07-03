package com.company.Interpreter.Service;

public class LanguageList<Item>
{
    private LanguageItem<Item> head;
    private int size;

    public LanguageList()
    {
        head = null;
        size = 0;
    }


    public void addBackward(Item value) throws Exception
    {
        if(head == null)
        {
            head = new LanguageItem<>(value, null);
        }
        else
        {
            LanguageItem<Item> buf = head;

            while(buf.next != null)
                buf = buf.next;

            buf.next = new LanguageItem<>(value, null);
        }

        ++size;
    }

    public void add(Item value, int position) throws Exception
    {
        if(head == null)
        {
            head = new LanguageItem<>(value, null);
        }
        else if(position <= size)
        {
            LanguageItem<Item> buf = head;

            for(int i = 0 ; i < position - 1; i ++)
                buf = buf.next;

            buf.next = new LanguageItem<>(value, buf.next);
        }
        else
            throw new Exception("Ошибка : выход за границы списка");

        ++size;
    }


    public Item get(int position) throws Exception
    {
        if(position < size)
        {
            LanguageItem<Item> buf = head;

            for(int i = 0 ; i < position; i ++)
                buf = buf.next;

            return buf.getValue();
        }
        else
            throw new Exception("Ошибка : выход за границы списка");
    }

    public void set(Item value, int position) throws Exception
    {
        if(position < size)
        {
            LanguageItem<Item> buf = head;

            for(int i = 0 ; i < position ; i ++)
                buf = buf.next;

            buf.setValue(value);
        }
        else
            throw new Exception("Ошибка : выход за границы списка");
    }

    public void remove(int position) throws Exception
    {
        if(position < size)
        {
            LanguageItem<Item> buf = head;

            for(int i = 0 ; i < position - 1; i ++)
                buf = buf.next;

            LanguageItem<Item> del = buf.next;
            buf.next = del.next;
            del = null;

            --size;
        }
        else
            throw new Exception("Ошибка : выход за границы списка");
    }

    public int getSize() { return size; }

}

class LanguageItem<Item>
{

    private Item value;
    public LanguageItem<Item> next;


    public LanguageItem(Item value, LanguageItem<Item> next)
    {
        this.value = value;
        this.next = next;
    }

    public Item getValue() { return value; }
    public void setValue(Item value) { this.value = value; }
}
