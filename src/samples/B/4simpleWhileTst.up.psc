PUSH D0
MOV SP D0
PUSH 0(D0)
ADD SP #1 SP
PUSH 1(D0)
ADD SP #1 SP
PUSH 1(D0)
RD 1(D0)
PUSH 0(D0)
RD 0(D0)
PUSH 1(D0)
WRTLNS 
L0:
PUSH 1(D0)
PUSH 0(D0)
CMPGTS
BRFS L1
PUSH 1(D0)
PUSH #1
SUBS
POP 1(D0)
PUSH 1(D0)
WRTLNS 
BR L0
L1: 
PUSH #20
POP 1(D0)
PUSH 1(D0)
WRTLNS 
L2:
PUSH 1(D0)
PUSH 0(D0)
CMPGES
BRFS L3
PUSH 1(D0)
PUSH #1
SUBS
POP 1(D0)
PUSH 1(D0)
WRTLNS 
BR L2
L3: 
PUSH #0
POP 1(D0)
PUSH 1(D0)
WRTLNS 
L4:
PUSH 1(D0)
PUSH 0(D0)
CMPLTS
BRFS L5
PUSH 1(D0)
PUSH #1
ADDS
POP 1(D0)
PUSH 1(D0)
WRTLNS 
BR L4
L5: 
PUSH #0
POP 1(D0)
PUSH 1(D0)
WRTLNS 
L6:
PUSH 1(D0)
PUSH 0(D0)
CMPLES
BRFS L7
PUSH 1(D0)
PUSH #1
ADDS
POP 1(D0)
PUSH 1(D0)
WRTLNS 
BR L6
L7: 
HLT
