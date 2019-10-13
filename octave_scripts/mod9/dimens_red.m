% just to test it with 100% accuracy

A = dlmread('infile.txt', ",", 0, 0);
[Ir, Ic] = size(A);
[U,S,R] = svd(A);

[Ur,Uc] = size(U);
[Sr,Sc] = size(S);
[Rr,Rc] = size(R);

printf("\nInput matrix\n");
for i = 1:Ir
  for j = 1:Ic
    printf("%f\t", A(i,j));
  endfor
  printf("\n");
endfor

aux = U*S*R
[Ar,Ac] = size(aux);

printf("\nReconstr\n");
for i = 1:Ar
  for j = 1:Ac
    printf("%f\t", aux(i,j));
  endfor
  printf("\n");
endfor

printf("\n\n");

% Compute the Frobenius norm error metric

sm = 0

for i = 1:Ar
  for j = 1:Ac
    sm += (aux(i,j)-A(i,j))^2
  endfor
endfor
    
printf("\n\nFrobenius norm: %d\n\n", sm^0.5);