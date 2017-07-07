:start
Now that you've listened to my boring story,
Let's begin.
You are all alone in a forest!
You come to a fork in a road:
Will you take a left, or a right?
:wait[input]
:if[input=right || input=Right]:
:jumpto 'fork_right'
:elif[input=left || input=Left]:
:jumpto 'fork_left'
:end