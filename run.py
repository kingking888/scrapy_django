import subprocess

def subprocess_cmd(command):
    process = subprocess.Popen(command, stdout=subprocess.PIPE, shell=True)
    proc_stdout = process.communicate()[0].strip()
    print(proc_stdout)


subprocess_cmd('python manage.py samakal_crawl')
subprocess_cmd('python manage.py ntvbd_crawl')
subprocess_cmd('python manage.py jagonews24_crawl')
subprocess_cmd('python manage.py jugantor_crawl')
subprocess_cmd('python manage.py dhakatribune_crawl')
subprocess_cmd('python manage.py bdprotidin_crawl')
subprocess_cmd('python manage.py ittefaq_crawl')
subprocess_cmd('python manage.py kalerkantho_crawl')
subprocess_cmd('python manage.py prothomalo_crawl')









