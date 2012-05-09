function xquant = quantentr(x,quantlvl)

% The function quantentr quantizes the signal x into quantlvl levels using
% a codebook defined by [0:1:quantlvl-1].
% Inputs:
% x - input signal
% quantlvl - a number of quantization levels
% Output:
% xquant - a quantized version of the input signal
% Author: Ervin Sejdic, March 4th, 2009.
% Revised by Joon Lee on May 14, 2010.
% Revised by Louis Mayaud on July 9th, 2010.
% Revised by Joon Lee on July 9th, 2010.

%Q = prctile(x,[5 95]);
%xmin=Q(1);
%xmax=Q(2);
xmin=min(x);
xmax=max(x);
quantstep = (xmax-xmin)/quantlvl;
if quantstep == 0
    xquant=zeros(size(x));
else
    partition = (xmin+quantstep):quantstep:(xmax-quantstep);
    xquant = quantiz(x,partition,(0:1:quantlvl-1));
end


% end of function
