function T = transferEntropyRank(X,Y,l,k,t,w,Q)

% This function computes the transfer entropy between time series X and Y,
% with the flow of information directed from X to Y, after ranking both X and Y. Probability density
% estimation is based on bin counting with fixed and equally-spaced bins.
% 
% For details, please see T Schreiber, "Measuring information transfer", Physical Review Letters, 85(2):461-464, 2000.
%
% Inputs:
% X: first time series in 1-D vector
% Y: second time series in 1-D vector
% l: block length for X
% k: block length for Y
% t: time lag in X from present to where the block of length l ends
% w: time lag in Y from present to where the block of length k ends
% Q: number of quantization levels for both X and Y
%
% Outputs:
% T: transfer entropy (bits)
%
%
% Written by Joon Lee on Oct. 26, 2011
% Revised by Joon Lee on Oct. 28, 2011


X=X(:)';
Y=Y(:)';

% ordinal sampling (ranking)
Nt=length(X);
[B,IX]=sort(X);
X(IX)=1:Nt;
[B,IX]=sort(Y);
Y(IX)=1:Nt;

% quantize X and Y according to fixed, equally-spaced bins
Xq=quantentr(X,Q); 
Yq=quantentr(Y,Q);

% go through the time series X and Y, and populate Xpat, Ypat, and Yt
Xpat=[]; Ypat=[]; Yt=[];
codeX=(Q.^((l-1):-1:0))';
codeY=(Q.^((k-1):-1:0))';

for i=max([l+t k+w]):1:min([length(Xq) length(Yq)])
    Xpat=[Xpat; Xq(i-l-t+1:i-t)*codeX];
    Ypat=[Ypat; Yq(i-k-w+1:i-w)*codeY];
    Yt=[Yt; Yq(i)];    
end

% compute transfer entropy
T=0;
idxDone=[];
N=length(Xpat);
for i=1:N
    if ~any(i==idxDone)        
        p1=sum(Xpat==Xpat(i) & Ypat==Ypat(i) & Yt==Yt(i))/N;
        p2=sum(Xpat==Xpat(i) & Ypat==Ypat(i) & Yt==Yt(i))/sum(Xpat==Xpat(i) & Ypat==Ypat(i));
        p3=sum(Ypat==Ypat(i) & Yt==Yt(i))/sum(Ypat==Ypat(i));
        T=T+p1*log2(p2/p3);        
        idxDone=[idxDone; find(Xpat==Xpat(i) & Ypat==Ypat(i) & Yt==Yt(i))];
        idxDone=unique(idxDone);
    end
end


