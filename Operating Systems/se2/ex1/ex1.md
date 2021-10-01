# Titulo

## 1.
### a.
    No endereço virtual:
        A page directory pointer tem 255 entradas, que é equivalente a 2^8, ou seja, tem 8 bits. 
        A page directory e a page table têm 1024 entradas, que é equivalente a 2^10, ou seja, cada table tem 10 bits.

        
        Como a page table entry tem 4 bytes e a page table tem 255 entradas, a página tem 1 Kib.
        Como a page table entry tem 4 bytes e tanto a page directory como a a page table tem 1024 entradas, cada página tem 4 Kib.	
        
        Como a page table entry tem 4 bytes e a page table tem 1024 entradas, a página tem 4 Kib. É possível calcular o offset do endereço virtual:
            Página com 4 Kib = 4 * 2^10 = 2^2 * 2^10 = 2^12
            O offset é representado por 12 bits

    Assim é possível obter a dimensão do endereço virtual. 8 bits da page directory pointer + 10 bits da page directory + 10 bits da page table + 12 bits de offset, determina-se que o endereço virtual tem 40 bits

### b.
	Espaço de endereço virtual = 2^40
	Espaço de endereço fisico = 2^40 / 4 = 2^40 / 2^2 = 2^40-2 = 2^38

	Sendo que o endereço fisico é igual à soma dos bits do offset mais os bits de controlo da PTE, os bits que para controlo que sobram na PTE são 38-12 = 26

### c.
    
    Adicionar 2 bits à page directory pointer.
   """"" Podemos estender a tabela de 1º nível até 1024 (2/10) entradas, ficando todos os níveis
	com 10 bits """""""""


### d.

Virtual address:

    in hexadecimal = 0x701053CC 
    in binary = 01110000 0001000001 0100111100 1100 <br>


O endereço físico da primeira tabela foi arbitráriamente escolhido como 0x20. 

Os primeiros 8 bits do endereço virtual = 0x70

Os 10 bits seguintes = 0x041 

Os 10 bits seguintes = 0x13C  

<br>

| Nível tabela | Address | PFN |
|--------------|---------|-----|
| 1º           | 0x20    | 70  |
| 2º           | 0x70    | 041 | 
| 3º           | 0x041   | 400 | 

<br>

O endereço final é:
    
    0x40000003CC


### e.

	Comente a seguinte afirmação: "Nesta arquitetura", cada acesso lógico à memória, correspondente a uma operação de fetch, load ou store do CPU, implica sempre quatro acessos físicos à memória."

.: Por cada acesso lógico acabamos a pagar sempre 4 acessos à memória para ir buscando várias tabelas e entradas das tabelas.</br>
Podemos diminuir esse número de acessos usando a cache - translation lookaside buffer - que mantém as últimas traduções válidas que foram feitas, de modo a serem reaproveitadas.</br>
Um endereço que foi usado tem uma grande probabilidade de ser usado logo a seguir ou muito em breve, portanto compensa manter o resultado da tradução em cache.
