#!/usr/bin/env python
"""
Start zookeeper
"""

from __future__ import print_function
import os
import re
import paramiko
import argparse
import subprocess
import signal
import sys

def signal_handler(signal, frame):
    sys.exit(0)

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
    dir_path = os.path.join('/tmp', 'zookeeper')
    file_path = os.path.join(dir_path, 'myid')
    command = 'mkdir -p {0} && echo "{1}" > {2}'.format(dir_path, serverId, file_path)
    sshClient.exec_command(command)

def removeDataDir(sshClient):
    dir_path = os.path.join('/tmp', 'zookeeper')
    command = 'rm -rf {}'.format(dir_path)
    sshClient.exec_command(command)

def startServer(sshClient, zookeeperHome):
    command_path = os.path.join(zookeeperHome, 'bin', 'zkServer.sh')
    command = 'nohup {0} start 2>&1'.format(command_path)
    stdin, stdout, stderr = sshClient.exec_command(command)
    status = stdout.read()
    sshClient.close()
    print(status)

def serverStatus(sshClient, zookeeperHome):
    command_path = os.path.join(zookeeperHome, 'bin', 'zkServer.sh')
    command = '{0} status 2>&1'.format(command_path)
    stdin, stdout, stderr = sshClient.exec_command(command)
    status = stdout.read()
    sshClient.close()
    print(status)

def stopServer(sshClient, zookeeperHome):
    command_path = os.path.join(zookeeperHome, 'bin', 'zkServer.sh')
    command = 'nohup {0} stop 2>&1'.format(command_path)
    stdin, stdout, stderr = sshClient.exec_command(command)

def configServer():
    print('Configuring Servers...')
    servers = []
    zookeeperHome = os.environ['ZOOKEEPER_HOME']
    configFilePath = os.path.join(zookeeperHome, 'conf', 'zoo.cfg')
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
    zookeeperHome = os.environ['ZOOKEEPER_HOME']
    sshClient = paramiko.SSHClient()
    paramiko.util.log_to_file("ZOOKEEPER_STARTER.log")
    sshClient.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    home = os.environ['HOME']
    user = os.environ['USER']
    pathToKey = os.path.join(home, '.ssh', 'inside_rsa')
    if not os.path.exists(pathToKey):
        pathToKey = os.path.join(home, '.ssh', 'id_rsa')
    if not os.path.exists(pathToKey):
        print('Error ssh keys not found. Exiting.', file=sys.stderr)
        sys.exit(1)
    privKey = paramiko.RSAKey.from_private_key_file(pathToKey)
    for server in serverList:
        try:
            sshClient.connect(server.hostname, username = user, pkey=privKey)
        except:
            error = 'Error: {} is currently not resonding. Exiting.'.format(server.hostname)
            print(error, file=sys.stderr)
            sys.exit(1)
        if mode is 0:
            createDataDir(sshClient, server.serverId)
            print('Starting: {0} and ID:{1}'.format(server.hostname, server.serverId))
            startServer(sshClient, zookeeperHome)
        elif mode is 1:
            print('Stopping: {0} and ID:{1}'.format(server.hostname, server.serverId))
            processId = stopServer(sshClient, zookeeperHome)
        elif mode is 2:
            print('Status: {0} and ID:{1}'.format(server.hostname, server.serverId))
            processId = serverStatus(sshClient, zookeeperHome)

def main():
    parser = argparse.ArgumentParser()
    opt = parser.add_mutually_exclusive_group(required=True)
    opt.add_argument('-on', action='store_true', default=False)
    opt.add_argument('-status', action='store_true', default=False)
    opt.add_argument('-off', action='store_true', default=False)
    args = parser.parse_args()

    servers = configServer()
    signal.signal(signal.SIGINT, signal_handler)

    if args.on:
        run(servers, 0)
    elif args.off:
        run(servers, 1)
    elif args.status:
        run(servers, 2)


    print('\nConfiguration Completed!')

if __name__ == "__main__":
    main()
