import subprocess

def subprocess_cmd(command):
    process = subprocess.Popen(command,stdout=subprocess.PIPE, shell=True)
    proc_stdout = process.communicate()[0].strip()
    print(proc_stdout)


subprocess_cmd('python manage.py prothomalo_crawl')
subprocess_cmd('python manage.py kalerkantho_crawl')
subprocess_cmd('python manage.py ittefaq_crawl')
subprocess_cmd('python manage.py bdprotidin_crawl')
subprocess_cmd('python manage.py dhakatribune_crawl')
