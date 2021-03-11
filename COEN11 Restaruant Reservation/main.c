
#include "global.h"

NODE *head;
NODE *tail;

pthread_mutex_t mymutex;

//main funtion with switch to call other functions
int main (int argc, char *argv[])
{
    
    int quitter = 0;
    char enteredname[LENGTH];
    int enteredsize;
    
    int key = atoi(argv[3]);
    
    
    if (argc < 3)
    {
        printf("ERROR\n");
        return 0;
    }
    
    //reading data from file into program
    readfile(argv[1], key);
    
    pthread_t tid;
    pthread_create(&tid, NULL, binwrite, (void*)argv[2]);
    
    
    while(1)
    {
        printf("Enter Code:\n");
        int enter;
        scanf("%d", &enter);
        
        
        switch (enter)
        {
                
            case 1:
                
                //prompting for user to enter party information
                printf("What is the name for the reservation?\n");
                scanf("%s", enteredname);
                
                while (1)
                {
                    //checking for name duplicate and forcing for new name if a same name is found
                    if (checkdup(enteredname) == 0)
                    {break;}
                    
                    printf("Name already used, enter a different name.\n");
                    scanf("%s",enteredname);
                }
                
                printf("What is the party size?\n");
                scanf("%d", &enteredsize);
                
                //to insert the information using insert function
                insert(enteredname, enteredsize);
                break;
                
            case 2: rm();
                break;
                
            case 3: list();
                break;
                
            case 4: sizesearch();
                break;
                
            case 5: backwardslist(head);
                break;
                
            case 6: backwardsnames(head);
                break;
                
            case 7: displaybinary(argv[2]);
                break;
                
            case 0:
                writefile(argv[1], key);
                quitter = 1;
                freeall(head);
                pthread_cancel(tid);
                break;
                
            default:
                printf("WRONG CODE.\n");
                break;
                
                
        }
        //to allow for quitting the program
        if (quitter == 1)
        {
            printf("EXIT\n");
            break;
        }
        
    }
}
