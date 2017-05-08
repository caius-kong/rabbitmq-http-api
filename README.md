# rabbitmq 控制管理

rabbit-console项目对RabbitMQ HTTP API进行了java封装，使得我们可以直接通过java语言对RabbitMQ进行控制管理。

> RabbitMQ HTTP API参考官方文档：http://localhost:15672/api/
>
> 注1：本机安装了rabbit-server
>
> 注2：如果是远程rabbit-server服务，localhost改为相应host ip


 引入项目依赖
----------

	 1、clone项目到本地：`$ git clone xxx`

	 2、在项目根路径运行:`$ mvn clean package -DskipTests`，/target下将产生jar包

	 3、引入jar包：直接添加jar包，或先发布到maven仓库再使用`<dependency/>`引入

> `-DskipTests` 表示打包时跳过测试。由于单元测试用的数据是我本地的数据，因此跳过测试，以防止打包失败。
>
> 发布jar包：`$ mvn deploy`

     <dependency>
		 <groupId>augtek.rabbitmq</groupId>
	     <artifactId>rabbitmq-console</artifactId>
	     <version>1.0</version>
	 </dependency>

创建一个用户
----------
	public void createUser(){
        // 设置Rabbit服务器信息，以及登录用户信息
        ServerConfig serverConfig = new ServerConfig("localhost", 15672, "guest", "guest");
        try{
            /**
             * 调用UserAPIs中的静态方法。
             * RoleEnum.MANAGEMENT.value() => "management"，即管理者，能查看所在vhost中的资源。（vhost默认"/"）
             * 例如想要读队列中的消息，首先得是"management"角色
             */
            boolean isSuccess = UserAPIs.createUser(serverConfig, "test_user", "123", RoleEnum.MANAGEMENT.value());
            System.out.println("create user: " + isSuccess);
        }catch(ResponseException e){
            // 打印响应异常信息
            LOG.error("error: {}", e.getMessage());
        }
    }

查询一个用户
----------
	public void findUser(){
        // 设置Rabbit服务器信息，以及登录用户信息
        ServerConfig serverConfig = new ServerConfig("localhost", 15672, "guest", "guest");
        try{
	        // 调用UserAPIs中的静态方法。
            JSONObject guest = UserAPIs.findUser(serverConfig, "guest");
            System.out.println(guest);
        }catch(ResponseException e){
            // 打印响应异常信息
            LOG.error("error: {}", e.getMessage());
        }
    }

> 可看到，此处查询返回一个json对象。
>
> 由于大部分查询API返回的结果不尽相同，但都是json数据，要么是json对象，要么是json数组，因此我们统一返回JSONObject | JSONArray。当你需要其中的数据时，请自行解析。
>
> 例如打印guest，可以看到：
> {"tags":"administrator","password_hash":"K2q7p7Kz2U5XidWh3VQx1FrmhZvnzJm76y2KOhBfOSW/C5cf","name":"guest","hashing_algorithm":"rabbit_password_hashing_sha256"}
>
> 获取“name”：`String name = (String)guest.get("name");`

