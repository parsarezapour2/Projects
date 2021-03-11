
#include "global.h"

//thread to autosave information to binary file every 15 seconds
//unlocking before every return statement
void* binwrite(void *arg)
{
    char *filename = (char *) arg;
    
    NODE *p;
    FILE *fp;
    
    while(1)
    {
        pthread_mutex_lock(&mymutex);
        printf("Autosaved.\n");
        fp = fopen(filename, "wb");
        
        if (fp == NULL)
        {
            printf("ERROR WITH FILE POINTER.\n");
            break;
        }
        
        p = head;
        
        
        while (p != NULL)
        {
            fwrite(p,sizeof(NODE), 1, fp);
            p = p->next;
        }
        
        fclose(fp);
        pthread_mutex_unlock(&mymutex);
        sleep(15);
    }
    
    return NULL;
}

//to write to text file after exiting program
void writefile(char *argv, int key)
{
    
    FILE *fp;
    
    fp = fopen(argv, "w");
    
    //if file doesnt exist
    if (fp == NULL)
    {
        printf("ERROR\n");
        return;
    }
    
    NODE *temp;
    
    temp = head;
    //header of document
    fprintf(fp, "Name          Size\n");
    fprintf(fp, "--------------------\n");
    
    //writing to txt file after header
    while (temp != NULL)
    {
        char estring[LENGTH];
        char *q = estring;
        sprintf(estring, "%s\t%d\n", temp->name, temp->group);
        
        while (*q != '\0')
        {
            *q = *q ^ key;
            q++;
        }
        
        fprintf(fp, "%s\n", estring);
        temp = temp->next;
        
    }
    
    printf("All contents of waiting list have been saved to file %s.\n", argv);
    fclose (fp);
    return;
}

//to read contents of text file and upload to program list
void readfile(char *argv, int key)
{
   
    FILE *fp;
    char string[LENGTH];
    char nam[LENGTH];
    int siz;
    
    fp = fopen(argv, "r");
    
    //if file doenst exist in the directory
    if (fp == NULL)
    {
        printf("No file found, data will be saved at EXIT.\n");
        return;
    }
    
    //to set file pointer to first set of data and ignore header
    fseek(fp, 40, SEEK_SET);
    
  while ( fgets(string, LENGTH+LENGTH, fp) != NULL)
  {
      int x;
      
      char *q = string;
      while (*q != '\n')
      {
          *q = *q ^ key;
          q++;
      }
      
      sscanf(string,"%s\t%d",nam, &siz);
      insert(nam, siz);
  }
    
    
    fclose(fp);
    printf("File %s has been decrypted and uploaded to the list.\n", argv);
    return;
    
}

//function to display contents of binary file
//unlocking before every return statement
void displaybinary(char *filename)
{
    
    FILE *fp;
    NODE *p;
    
    pthread_mutex_lock(&mymutex);
    if ((fp = fopen(filename, "rb")) == NULL)
    {
        printf("Nothing currently stored in binary file to display.\n");
        return;
    }
    
    if ( (p = (NODE *)malloc(sizeof(NODE))) == NULL)
    {
        printf("Memory could not be allocated using malloc.\n");
        return;
    }
    
    while (fread(p, sizeof(NODE), 1, fp) == 1)
    {
        if (p == NULL)
        {
            return;
        }
        
        printf("%s's reservation for %d.\n", p->name, p->group);
    }
    fclose(fp);
    pthread_mutex_unlock(&mymutex);
}




