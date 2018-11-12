def test_var_args(f_arg, *argv):
    print("first normal arg:", f_arg)
    for arg in argv:
        print("another arg through *argv:", arg)
# test_var_args('yasoob', 'python', 'eggs', 'test')


''' **XX 是 传K-V对'''
def greet_me(**kwargs):
    for key, value in kwargs.items():
        print("{0} == {1}".format(key, value))

# greet_me(name='qfl',age=12)


def test_args_kwargs(arg1, arg2, arg3):
    print("arg1:", arg1)
    print("arg2:", arg2)
    print("arg3:", arg3)

args = ("two", 3, 5)
# test_args_kwargs(*args)

''' 也就是说传如的参数顺序可以通过 **XX 来变得不一样。'''
kwargs = {"arg3": 3, "arg2": "two", "arg1": 5}
test_args_kwargs(**kwargs)