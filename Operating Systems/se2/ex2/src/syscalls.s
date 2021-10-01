.text
.global xgetpid, xopen, xwrite, xclose

xgetpid:
    movq $39, %rax
    syscall
    ret
xopen:
    movq $2, %rax
    syscall
    ret
xwrite:
    movq $1, %rax
    syscall
    ret
xclose:
    movq $3, %rax
    syscall
    ret
.end
