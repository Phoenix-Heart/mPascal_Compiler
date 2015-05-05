PUSH D0
MOV SP D0
PUSH 0(D0)
ADD SP #1 SP
PUSH 1(D0)
ADD SP #1 SP
PUSH 2(D0)
ADD SP #1 SP
PUSH 3(D0)
ADD SP #1 SP
PUSH 4(D0)
ADD SP #1 SP
PUSH 5(D0)
ADD SP #1 SP
PUSH #2
PUSH #5
PUSH 4(D0)
PUSH 3(D0)
ADDS
PUSH 3(D0)
PUSH 4(D0)
SUBS
PUSH 4(D0)
PUSH 5(D0)
MULS
PUSH 3(D0)
PUSH 2(D0)
MULS
PUSH #'e = '
WRTS 
PUSH 1(D0)
WRTS 
PUSH #': should be 14'
WRTLNS #': should be 14'
PUSH #'f = '
WRTS 
PUSH 0(D0)
WRTS 
PUSH #': should be 15'
WRTLNS #': should be 15'
PUSH #'a = '
WRTS 
PUSH 5(D0)
WRTS 
PUSH #': should be 7'
WRTLNS #': should be 7'
PUSH 1(D0)
PUSH 0(D0)
MODS
PUSH #'a = '
WRTS 
PUSH 5(D0)
WRTS 
PUSH #': should be 14'
WRTLNS #': should be 14'
PUSH #'b = '
WRTS 
PUSH 4(D0)
WRTS 
PUSH #': should be 2'
WRTLNS #': should be 2'
PUSH 5(D0)
PUSH 4(D0)
DIVS
PUSH #'b = '
WRTS 
PUSH 4(D0)
WRTS 
PUSH #': should be 7'
WRTLNS #': should be 7'
PUSH #'c = '
WRTS 
PUSH 3(D0)
WRTS 
PUSH #': should be 5'
WRTLNS #': should be 5'
PUSH 0(D0)
PUSH 3(D0)
PUSH 4(D0)
MULS
DIVS
PUSH #'c = '
WRTS 
PUSH 3(D0)
WRTS 
PUSH #': should be 21'
WRTLNS #': should be 21'
