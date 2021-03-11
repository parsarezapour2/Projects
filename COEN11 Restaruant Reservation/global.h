
/*
 
 Parsa Rezapour
 COEN 11L TUESDAY 2:15PM
 February 25 2019
 //Lab9//
 threading
 
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#define SIZE 10
#define LENGTH 20

//struct NODE
typedef struct thing
{
    char name[LENGTH];
    int group;
    struct thing *next;
} NODE;


//prototypes

void insert(char *, int);
void rm();
void list();
void sizesearch();
int checkdup(char *);
void readfile(char *, int);
void writefile(char *, int);
void freeall(NODE *);
void backwardsnames(NODE *);
char* strrev(char *);
void backwardslist( NODE *);

//prototypes new to Lab 9
void binread(char *);
void* binwrite(void *);
void displaybinary(char *);


extern NODE *head;
extern NODE *tail;

extern pthread_mutex_t mymutex;
