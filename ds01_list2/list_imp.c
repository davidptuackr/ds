#include "list_prototype.h"
#include <stdlib.h>
#include <stdio.h>

SLList* create_list()
{
    SLList* list = (SLList*)malloc(sizeof(SLList));
    list->head = NULL;
    list->tail = NULL;
    list->cnt = 0;

    return list;
}

SLNode* create_node(int data)
{
    SLNode* node = (SLNode*)malloc(sizeof(SLNode));
    node->data = data;
    node->next = NULL;

    return node;
}

void insert_tail(SLList* list, int data)
{
    if (list->head == NULL)
    {
        list->head = create_node(data);
        list->tail = list->head;
    }
    else
    {
        list->tail->next = create_node(data);
        list->tail = list->tail->next;
    }
    list->cnt++;
}

void insert_at(SLList* list, int data, int loc)
{
    /*
    * loc : 0 ~ cnt-1
    * ex. loc : 0 -> head 밀어내고 새 노드를 head로 삼음
    * ex. loc : cnt-1 -> tail 밀어냄. tail은 여전히 기존 요소임
    */

    SLNode* iter = list->head;
    int move = loc;

    while (iter != NULL && --move > 0)
    {
        iter = iter->next;
    }

    if (iter != NULL)
    {
        if (loc == 0)
        {
            list->head = create_node(data);
            list->head->next = iter;
        }
        else
        {
            SLNode* new_node = create_node(data);
            new_node->next = iter->next;
            iter->next = new_node;
        }
        list->cnt++;
    }
}

SLNode* delete_tail(SLList* list)
{
    SLNode* iter = list->head;
    SLNode* ret = NULL;

    while ((list->head != list->tail) && (iter != NULL && iter->next != list->tail))
    {
        iter = iter->next;
    }

    if (iter != NULL)
    {
        if (iter == list->tail)
        {
            ret = iter;
            list->head = NULL;
            list->tail = NULL;
        }
        else
        {
            ret = iter->next;
            list->tail = iter;
            list->tail->next = NULL;
        }
        list->cnt--;
        ret->next = NULL;
    }

    return ret;
}

SLNode* delete_at(SLList* list, int loc)
{
    // insert_at과 동일하게 위치 설정

    SLNode* iter = list->head;
    SLNode* ret = NULL;
    int move = loc;

    while (iter != NULL && --move > 0)
    {
        iter = iter->next;
    }

    if (iter != NULL)
    {
        if (loc == 0)
        {
            ret = iter;
            if (list->head == list->tail)
            {
                list->head = NULL;
                list->tail = NULL;
            }
            else
            {
                list->head = list->head->next;
            }
        }
        else if (loc == (list->cnt) - 1)
        {
            ret = iter;
            list->tail = iter;
            iter->next = NULL;
        }
        else
        {
            ret = iter->next;
            iter->next = ret->next;
        }
        list->cnt--;
        ret->next = NULL;
    }

    return ret;
}

SLNode* search_node(SLList* list, int loc)
{
    SLNode* iter = list->head;
    SLNode* ret;
    int move = loc;

    while (iter != NULL && --move > 0)
    {
        iter = iter->next;
    }

    if (iter != NULL)
    {
        ret = iter;
    }
    else
    {
        ret = NULL;
    }
    return ret;
}

void describe_list(SLList* list)
{
    SLNode* iter = list->head;
    int loc = 0;
    if (iter == NULL)
    {
        printf("EMPTY LIST\n");
        return;
    }

    printf("HEAD : [%p, %d]\t", list->head, list->head->data);
    printf("TAIL : [%p, %d]\t", list->tail, list->tail->data);
    printf("SIZE : %d\n", list->cnt);

    while (iter != NULL)
    {
        printf("LIST[%d] : %d\t", loc, iter->data);
        if ((loc+1) % 3 == 0)
        {
            printf("\n\n");
        }
        loc++;
        iter = iter->next;
    }

    printf("\n\n");
}

