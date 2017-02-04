#!/usr/bin/env python
"""
Start storm
"""

import os, re
import paramiko
import argparse
import subprocess

class Client:
    def __init__(self, hostname, serverId):
        self.hostname = hostname+".cs.colostate.edu"
        self.serverId = serverId
    def __str__(self):
        return '[{0}, {1}]'.format(self.hostname, self.serverId)
    def __repr__(self):
        return '[{0}, {1}]'.format(self.hostname, self.serverId)

def get_match_groups(output, regex):
    match = regex.search(output)
    if not match:
        raise Exception('Invalid output: ' + output.rstrip())
    return match.groups()

def createDataDir(sshClient, serverId):
    dir_path = os.path.join('/tmp', 'storm')
    file_path = os.path.join(dir_path, 'myid')
    command = 'mkdir -p {0} && echo "{1}" > {2}'.format(dir_path, serverId, file_path)
    sshClient.exec_command(command)

def removeDataDir(sshClient, serverId):
    dir_path = os.path.join('/tmp', 'storm')
    command = 'rmdir -rf {}'.format(dir_path)
    sshClient.exec_command(command)

def startServer(sshClient, eyeHome):
    command_path = os.path.join(eyeHome, 'script', 'runStorm.sh')
    command = 'nohup {0} start > /dev/null 2>&1'.format(command_path)
    stdin, stdout, stderr = sshClient.exec_command(command)

def stopServer(sshClient, eyeHome):
    command_path = os.path.join(eyeHome, 'script', 'runStorm.sh')
    command = 'nohup {0} stop > /dev/null 2>&1'.format(command_path)
    stdin, stdout, stderr = sshClient.exec_command(command)

def configServer():
    print('Configuring Servers...')
    servers = []
    stormHome = os.environ['STORM_HOME']
    eyeHome = os.environ['EYE_HOME']
    configFilePath = os.path.join(stormHome, 'conf', 'storm.yml')
    with open(configFilePath) as configFile:
        lines = configFile.readlines()
    for line in lines:
        matcher = re.compile('server.([0-9]+)=(.*):[0-9]+:[0-9]+')
        try:
            serverInfo = get_match_groups(line, matcher)
            servers.append(Client(serverInfo[1], serverInfo[0]))
        except:
            pass
    return servers

def run(serverList, mode):
    stormHome = os.environ['STORM_HOME']
    sshClient = paramiko.SSHClient()
    sshClient.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    home = os.environ['HOME']
    user = os.environ['USER']
    pathToKey = os.path.join(home, '.ssh', 'inside_rsa')
    if not os.path.exists(pathToKey):
        pathToKey = os.path.join(home, '.ssh', is_rsa)
    if not os.path.exists(pathToKey):
        print('Error ssh keys not found. Exiting.', file=sys.stderr)
        sys.exit(1)
    privKey = paramiko.RSAKey.from_private_key_file(pathToKey)
    for server in serverList:
        sshClient.connect(server.hostname, username = user, pkey=privKey)
        createDataDir(sshClient, server.serverId)
        if mode:
            createDataDir(sshClient, server.serverId)
            startServer(sshClient, eyeHome)
            print('Starting: {0} and ID:{1}'.format(server.hostname, server.serverId))
        else:
            processId = stopServer(sshClient, eyeHome)
            print('Stopping: {0} and ID:{1}'.format(server.hostname, server.serverId))

def main():
    parser = argparse.ArgumentParser()
    args = parser.parse_args()

    servers = configServer()

    run(servers, True)

    print('\nConfiguration Completed!')

if __name__ == "__main__":
    main()
