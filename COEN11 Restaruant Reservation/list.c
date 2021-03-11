#include "global.h"

//to list all reservations
void list()
{
    //to check if the list is empty
    if (head == NULL)
    {
        printf("No reservations scheduled.\n");
    }
    
    else
    {
        NODE *p;
        p = head;
        
        while (p != NULL)
        {
            /*if (p == head)
             {
             printf("(HEAD) ");
             }
             if (p == tail)
             {
             printf("(TAIL) ");
             }*/
            printf("%s's reservation for a party of %d.\n", p->name, p->group);
            p = p->next;
        }
    }
    
    return;
    
}

//to insert a NODE
void insert(char s[], int h)
{
    
    pthread_mutex_lock(&mymutex);
    //allocating memory for struct using malloc
    NODE *ptr;
    ptr = (NODE*) malloc( sizeof(NODE));
    
    //To check if the memory was allocated and to confirm that the data has been placed within the NODE
    
    // pthread_mutex_lock(&mymutex);
    if (ptr == NULL)
    {
        printf("ERROR, Memory was NOT allocated using malloc.\n");
    }
    else
    {
        
        printf("Memory successfully allocated using malloc.\n");
        strcpy(ptr->name, s);
        ptr->group = h;
        printf("%s's party of %d is confirmed.\n", s, h);
    }
    
    if (head == NULL)
    {
        ptr->next = NULL;
        head = ptr;
        tail = ptr;
    }
    else
    {
        ptr->next = NULL;
        tail->next = ptr;
        tail = ptr;
    }
    pthread_mutex_unlock(&mymutex);
    
    // pthread_mutex_unlock(&mymutex);
    return;
}

//to remove a NODE with group size less than or equal to the table size opening
void rm()
{
    NODE *p = head;
    NODE *a = head;
    
    pthread_mutex_lock(&mymutex);
    
    //if there are no NODES in the linked list
    if (head == NULL)
    {
        printf("There are no reservations to cancel.\n");
        pthread_mutex_unlock(&mymutex);
        return;
    }
    else
    {
        printf("What is the size of the table opening?\n");
        int x;
        scanf("%d", &x);
        
        
        //if the head party is the one to be removed and it is the only party on the waiting list
        if (p != NULL && p->group <= x)
        {
            printf("%s's party of %d has been seated.\n", head->name, head->group);
            head = head->next;
            free(p);
            pthread_mutex_unlock(&mymutex);
            return;
        }
        
        //if there is more than one party on the waiting list
        
        while (p != NULL && (!(p->group <= x)))
        {
            a = p;
            p = p->next;
            
        }
        
        if (p == NULL)
        {
            printf("All parties have a party size bigger than the table size opening.\n");
            pthread_mutex_unlock(&mymutex);
            return;
        }
        
        if (a->next->next == NULL)
        {
            tail = a;
        }
        
        //unlink the party that has been seated and free the memory
        printf("%s's party of %d has been seated.\n", p->name, p->group);
        a->next = p->next;
        free(p);
        pthread_mutex_unlock(&mymutex);
    }
}

//recursive function to free all nodes at exit of file
void freeall(NODE *x)
{
    
    if (x == NULL)
    {
        return;
    }
    
    NODE *p;
    p = x;
    
    free(p);
    freeall(x->next);
    
}

//recursive function to print names with reversed characters
void backwardsnames(NODE *x)
{
    
    if (x == NULL)
    {
        return;
    }
    
    char string[LENGTH];
    
    strcpy(string, x->name);
    
    char *strptr;
    strptr = string;
    
    int end;
    end = strlen(string) - 1;
    int beg = 0;
    
    strrev(string);
    
    
    printf("%s\n", string);
    
    backwardsnames(x->next);
    
    
    
}

//recursive function to print list backwards
void backwardslist(NODE *x)
{
    if (x == NULL)
    {
        return;
        
    }
    
    backwardslist (x->next);
    printf("%s's party of %d.\n", x->name, x->group);
    
    
}

//function to flip reverse the characters of a string
char *strrev(char *str)
{
    char *p1, *p2;
    
    if (! str || ! *str)
        return str;
    for (p1 = str, p2 = str + strlen(str) - 1; p2 > p1; ++p1, --p2)
    {
        *p1 ^= *p2;
        *p2 ^= *p1;
        *p1 ^= *p2;
    }
    return str;
}

//to check for a name duplicate
int checkdup(char o[LENGTH])
{
    
    NODE *p;
    p = head;
    
    while (p != NULL)
    {
        
        if (strcmp(p->name, o) == 0)
        {
            return 1;
            break;
        }
        
        p = p->next;
    }
    
    
    return 0;
}

//to search for a certain sized party in the linked list
void sizesearch()
{
    int x;
    int flag = 0;
    
    printf("What is the size?\n");
    scanf("%d", &x);
    
    NODE *current;
    current = head;
    
    
    while (current != NULL )
    {
        if (current->group <= x)
        {
            printf("%s's party size is %d.\n", current->name, current->group);
            flag = 1;
            current = current->next;
        }
    }
    
    if (flag == 0)
    {
        printf("All parties are larger than this size.\n");
        return;
    }
    
}
