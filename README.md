serverAddr = "47.122.95.110"
serverPort = 7000

auth.method = "token"
auth.token = "A$@B<62yQ!q5c4=(>uF@"

[[proxies]]
name = "ssh-4090D"
type = "tcp"
localIP = "127.0.0.1"
localPort = 22
remotePort = 11809


[[proxies]]
name = "ollama-4090D"
type = "tcp"
localIP = "127.0.0.1"
localPort = 11434
remotePort = 18080
