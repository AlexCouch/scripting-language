:wait[input]
:if[input=right || input=Right]:
:jumpto 'fork_right'
:elif[input=left || input=Left]:
:jumpto 'fork_left'