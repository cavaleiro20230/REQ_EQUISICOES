import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

// Main application class
public class ProcurementSystem {
    public static void main(String[] args) {
        ProcurementApp app = new ProcurementApp();
        app.start();
    }
}

// Core application class
class ProcurementApp {
    private List<User> users = new ArrayList<>();
    private List<Request> requests = new ArrayList<>();
    private List<Acquisition> acquisitions = new ArrayList<>();
    private User currentUser;
    private Scanner scanner = new Scanner(System.in);
    
    public ProcurementApp() {
        // Initialize with some sample data
        users.add(new User("admin", "admin", "Administrator", UserRole.ADMIN));
        users.add(new User("manager", "manager", "Department Manager", UserRole.MANAGER));
        users.add(new User("employee", "employee", "Regular Employee", UserRole.EMPLOYEE));
        
        // Sample request
        User requester = users.get(2);
        Request request = new Request(
            UUID.randomUUID().toString(),
            "Office Supplies",
            "Need new notebooks and pens",
            500.0,
            requester,
            RequestStatus.PENDING
        );
        requests.add(request);
        
        // Sample acquisition
        Acquisition acquisition = new Acquisition(
            UUID.randomUUID().toString(),
            request,
            LocalDate.now(),
            "Office Depot",
            490.0,
            AcquisitionStatus.IN_PROGRESS
        );
        acquisitions.add(acquisition);
    }
    
    public void start() {
        System.out.println("=== SISTEMA DE REQUISIÇÕES E AQUISIÇÕES ===");
        
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = loginMenu();
            } else {
                running = mainMenu();
            }
        }
        
        System.out.println("Sistema encerrado. Obrigado!");
    }
    
    private boolean loginMenu() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Usuário: ");
        String username = scanner.nextLine();
        
        System.out.print("Senha: ");
        String password = scanner.nextLine();
        
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Login bem-sucedido! Bem-vindo, " + user.getName());
                return true;
            }
        }
        
        System.out.println("Usuário ou senha inválidos!");
        return true;
    }
    
    private boolean mainMenu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("Usuário atual: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        System.out.println("1. Gerenciar Requisições");
        System.out.println("2. Gerenciar Aquisições");
        System.out.println("3. Gerenciar Usuários (apenas Admin)");
        System.out.println("4. Logout");
        System.out.println("5. Sair do Sistema");
        System.out.print("Escolha uma opção: ");
        
        String option = scanner.nextLine();
        
        switch (option) {
            case "1":
                requestMenu();
                break;
            case "2":
                acquisitionMenu();
                break;
            case "3":
                if (currentUser.getRole() == UserRole.ADMIN) {
                    userMenu();
                } else {
                    System.out.println("Acesso negado! Apenas administradores podem gerenciar usuários.");
                }
                break;
            case "4":
                currentUser = null;
                System.out.println("Logout realizado com sucesso!");
                break;
            case "5":
                return false;
            default:
                System.out.println("Opção inválida!");
        }
        
        return true;
    }
    
    private void requestMenu() {
        boolean inMenu = true;
        
        while (inMenu) {
            System.out.println("\n=== GERENCIAMENTO DE REQUISIÇÕES ===");
            System.out.println("1. Listar Todas as Requisições");
            System.out.println("2. Criar Nova Requisição");
            System.out.println("3. Visualizar Detalhes de uma Requisição");
            System.out.println("4. Aprovar/Rejeitar Requisição (apenas Manager/Admin)");
            System.out.println("5. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            
            String option = scanner.nextLine();
            
            switch (option) {
                case "1":
                    listRequests();
                    break;
                case "2":
                    createRequest();
                    break;
                case "3":
                    viewRequestDetails();
                    break;
                case "4":
                    if (currentUser.getRole() == UserRole.ADMIN || currentUser.getRole() == UserRole.MANAGER) {
                        approveRejectRequest();
                    } else {
                        System.out.println("Acesso negado! Apenas gerentes e administradores podem aprovar/rejeitar requisições.");
                    }
                    break;
                case "5":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    
    private void acquisitionMenu() {
        boolean inMenu = true;
        
        while (inMenu) {
            System.out.println("\n=== GERENCIAMENTO DE AQUISIÇÕES ===");
            System.out.println("1. Listar Todas as Aquisições");
            System.out.println("2. Criar Nova Aquisição (a partir de requisição aprovada)");
            System.out.println("3. Visualizar Detalhes de uma Aquisição");
            System.out.println("4. Atualizar Status de Aquisição");
            System.out.println("5. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            
            String option = scanner.nextLine();
            
            switch (option) {
                case "1":
                    listAcquisitions();
                    break;
                case "2":
                    createAcquisition();
                    break;
                case "3":
                    viewAcquisitionDetails();
                    break;
                case "4":
                    updateAcquisitionStatus();
                    break;
                case "5":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    
    private void userMenu() {
        boolean inMenu = true;
        
        while (inMenu) {
            System.out.println("\n=== GERENCIAMENTO DE USUÁRIOS ===");
            System.out.println("1. Listar Todos os Usuários");
            System.out.println("2. Adicionar Novo Usuário");
            System.out.println("3. Editar Usuário");
            System.out.println("4. Remover Usuário");
            System.out.println("5. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            
            String option = scanner.nextLine();
            
            switch (option) {
                case "1":
                    listUsers();
                    break;
                case "2":
                    addUser();
                    break;
                case "3":
                    editUser();
                    break;
                case "4":
                    removeUser();
                    break;
                case "5":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    
    // Request management methods
    private void listRequests() {
        System.out.println("\n=== LISTA DE REQUISIÇÕES ===");
        if (requests.isEmpty()) {
            System.out.println("Não há requisições cadastradas.");
            return;
        }
        
        System.out.println("ID | Título | Valor | Status | Solicitante");
        System.out.println("------------------------------------------");
        
        for (Request request : requests) {
            System.out.printf("%s | %s | R$%.2f | %s | %s%n",
                request.getId().substring(0, 8),
                request.getTitle(),
                request.getEstimatedValue(),
                request.getStatus(),
                request.getRequester().getName()
            );
        }
    }
    
    private void createRequest() {
        System.out.println("\n=== CRIAR NOVA REQUISIÇÃO ===");
        
        System.out.print("Título: ");
        String title = scanner.nextLine();
        
        System.out.print("Descrição: ");
        String description = scanner.nextLine();
        
        System.out.print("Valor Estimado (R$): ");
        double estimatedValue = Double.parseDouble(scanner.nextLine());
        
        Request request = new Request(
            UUID.randomUUID().toString(),
            title,
            description,
            estimatedValue,
            currentUser,
            RequestStatus.PENDING
        );
        
        requests.add(request);
        System.out.println("Requisição criada com sucesso! ID: " + request.getId().substring(0, 8));
    }
    
    private void viewRequestDetails() {
        System.out.println("\n=== DETALHES DA REQUISIÇÃO ===");
        System.out.print("Digite o ID da requisição: ");
        String id = scanner.nextLine();
        
        Request request = findRequestById(id);
        
        if (request == null) {
            System.out.println("Requisição não encontrada!");
            return;
        }
        
        System.out.println("ID: " + request.getId());
        System.out.println("Título: " + request.getTitle());
        System.out.println("Descrição: " + request.getDescription());
        System.out.println("Valor Estimado: R$" + request.getEstimatedValue());
        System.out.println("Status: " + request.getStatus());
        System.out.println("Solicitante: " + request.getRequester().getName());
        System.out.println("Data de Criação: " + request.getCreationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
    
    private void approveRejectRequest() {
        System.out.println("\n=== APROVAR/REJEITAR REQUISIÇÃO ===");
        System.out.print("Digite o ID da requisição: ");
        String id = scanner.nextLine();
        
        Request request = findRequestById(id);
        
        if (request == null) {
            System.out.println("Requisição não encontrada!");
            return;
        }
        
        if (request.getStatus() != RequestStatus.PENDING) {
            System.out.println("Esta requisição já foi " + 
                (request.getStatus() == RequestStatus.APPROVED ? "aprovada" : "rejeitada") + "!");
            return;
        }
        
        System.out.println("Título: " + request.getTitle());
        System.out.println("Valor: R$" + request.getEstimatedValue());
        System.out.println("1. Aprovar");
        System.out.println("2. Rejeitar");
        System.out.print("Escolha uma opção: ");
        
        String option = scanner.nextLine();
        
        if (option.equals("1")) {
            request.setStatus(RequestStatus.APPROVED);
            System.out.println("Requisição aprovada com sucesso!");
        } else if (option.equals("2")) {
            request.setStatus(RequestStatus.REJECTED);
            System.out.println("Requisição rejeitada!");
        } else {
            System.out.println("Opção inválida!");
        }
    }
    
    // Acquisition management methods
    private void listAcquisitions() {
        System.out.println("\n=== LISTA DE AQUISIÇÕES ===");
        if (acquisitions.isEmpty()) {
            System.out.println("Não há aquisições cadastradas.");
            return;
        }
        
        System.out.println("ID | Requisição | Fornecedor | Valor | Status");
        System.out.println("------------------------------------------");
        
        for (Acquisition acquisition : acquisitions) {
            System.out.printf("%s | %s | %s | R$%.2f | %s%n",
                acquisition.getId().substring(0, 8),
                acquisition.getRequest().getTitle(),
                acquisition.getSupplier(),
                acquisition.getActualValue(),
                acquisition.getStatus()
            );
        }
    }
    
    private void createAcquisition() {
        System.out.println("\n=== CRIAR NOVA AQUISIÇÃO ===");
        
        // List approved requests without acquisitions
        List<Request> approvedRequests = new ArrayList<>();
        for (Request request : requests) {
            if (request.getStatus() == RequestStatus.APPROVED && !hasAcquisition(request)) {
                approvedRequests.add(request);
            }
        }
        
        if (approvedRequests.isEmpty()) {
            System.out.println("Não há requisições aprovadas sem aquisições!");
            return;
        }
        
        System.out.println("Requisições aprovadas disponíveis:");
        for (int i = 0; i < approvedRequests.size(); i++) {
            Request request = approvedRequests.get(i);
            System.out.printf("%d. %s - %s (R$%.2f)%n", 
                i + 1, 
                request.getId().substring(0, 8), 
                request.getTitle(), 
                request.getEstimatedValue()
            );
        }
        
        System.out.print("Selecione o número da requisição: ");
        int requestIndex = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (requestIndex < 0 || requestIndex >= approvedRequests.size()) {
            System.out.println("Opção inválida!");
            return;
        }
        
        Request selectedRequest = approvedRequests.get(requestIndex);
        
        System.out.print("Fornecedor: ");
        String supplier = scanner.nextLine();
        
        System.out.print("Valor Real (R$): ");
        double actualValue = Double.parseDouble(scanner.nextLine());
        
        Acquisition acquisition = new Acquisition(
            UUID.randomUUID().toString(),
            selectedRequest,
            LocalDate.now(),
            supplier,
            actualValue,
            AcquisitionStatus.IN_PROGRESS
        );
        
        acquisitions.add(acquisition);
        System.out.println("Aquisição criada com sucesso! ID: " + acquisition.getId().substring(0, 8));
    }
    
    private void viewAcquisitionDetails() {
        System.out.println("\n=== DETALHES DA AQUISIÇÃO ===");
        System.out.print("Digite o ID da aquisição: ");
        String id = scanner.nextLine();
        
        Acquisition acquisition = findAcquisitionById(id);
        
        if (acquisition == null) {
            System.out.println("Aquisição não encontrada!");
            return;
        }
        
        System.out.println("ID: " + acquisition.getId());
        System.out.println("Requisição: " + acquisition.getRequest().getTitle());
        System.out.println("Fornecedor: " + acquisition.getSupplier());
        System.out.println("Valor Real: R$" + acquisition.getActualValue());
        System.out.println("Status: " + acquisition.getStatus());
        System.out.println("Data de Aquisição: " + acquisition.getAcquisitionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
    
    private void updateAcquisitionStatus() {
        System.out.println("\n=== ATUALIZAR STATUS DE AQUISIÇÃO ===");
        System.out.print("Digite o ID da aquisição: ");
        String id = scanner.nextLine();
        
        Acquisition acquisition = findAcquisitionById(id);
        
        if (acquisition == null) {
            System.out.println("Aquisição não encontrada!");
            return;
        }
        
        System.out.println("Status atual: " + acquisition.getStatus());
        System.out.println("Selecione o novo status:");
        System.out.println("1. Em Andamento");
        System.out.println("2. Entregue");
        System.out.println("3. Cancelada");
        System.out.print("Escolha uma opção: ");
        
        String option = scanner.nextLine();
        
        switch (option) {
            case "1":
                acquisition.setStatus(AcquisitionStatus.IN_PROGRESS);
                break;
            case "2":
                acquisition.setStatus(AcquisitionStatus.DELIVERED);
                break;
            case "3":
                acquisition.setStatus(AcquisitionStatus.CANCELLED);
                break;
            default:
                System.out.println("Opção inválida!");
                return;
        }
        
        System.out.println("Status atualizado com sucesso!");
    }
    
    // User management methods
    private void listUsers() {
        System.out.println("\n=== LISTA DE USUÁRIOS ===");
        System.out.println("Username | Nome | Função");
        System.out.println("------------------------");
        
        for (User user : users) {
            System.out.printf("%s | %s | %s%n",
                user.getUsername(),
                user.getName(),
                user.getRole()
            );
        }
    }
    
    private void addUser() {
        System.out.println("\n=== ADICIONAR NOVO USUÁRIO ===");
        
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        // Check if username already exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("Este nome de usuário já existe!");
                return;
            }
        }
        
        System.out.print("Senha: ");
        String password = scanner.nextLine();
        
        System.out.print("Nome: ");
        String name = scanner.nextLine();
        
        System.out.println("Função:");
        System.out.println("1. Administrador");
        System.out.println("2. Gerente");
        System.out.println("3. Funcionário");
        System.out.print("Escolha uma opção: ");
        
        String option = scanner.nextLine();
        UserRole role;
        
        switch (option) {
            case "1":
                role = UserRole.ADMIN;
                break;
            case "2":
                role = UserRole.MANAGER;
                break;
            case "3":
                role = UserRole.EMPLOYEE;
                break;
            default:
                System.out.println("Opção inválida! Definindo como Funcionário por padrão.");
                role = UserRole.EMPLOYEE;
        }
        
        User newUser = new User(username, password, name, role);
        users.add(newUser);
        
        System.out.println("Usuário adicionado com sucesso!");
    }
    
    private void editUser() {
        System.out.println("\n=== EDITAR USUÁRIO ===");
        System.out.print("Digite o username do usuário: ");
        String username = scanner.nextLine();
        
        User user = findUserByUsername(username);
        
        if (user == null) {
            System.out.println("Usuário não encontrado!");
            return;
        }
        
        System.out.println("Deixe em branco para manter o valor atual.");
        
        System.out.print("Nova senha (atual: " + user.getPassword() + "): ");
        String password = scanner.nextLine();
        if (!password.isEmpty()) {
            user.setPassword(password);
        }
        
        System.out.print("Novo nome (atual: " + user.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            user.setName(name);
        }
        
        System.out.println("Nova função (atual: " + user.getRole() + "):");
        System.out.println("1. Administrador");
        System.out.println("2. Gerente");
        System.out.println("3. Funcionário");
        System.out.println("4. Manter atual");
        System.out.print("Escolha uma opção: ");
        
        String option = scanner.nextLine();
        
        switch (option) {
            case "1":
                user.setRole(UserRole.ADMIN);
                break;
            case "2":
                user.setRole(UserRole.MANAGER);
                break;
            case "3":
                user.setRole(UserRole.EMPLOYEE);
                break;
            case "4":
                // Keep current role
                break;
            default:
                System.out.println("Opção inválida! Mantendo a função atual.");
        }
        
        System.out.println("Usuário atualizado com sucesso!");
    }
    
    private void removeUser() {
        System.out.println("\n=== REMOVER USUÁRIO ===");
        System.out.print("Digite o username do usuário: ");
        String username = scanner.nextLine();
        
        User user = findUserByUsername(username);
        
        if (user == null) {
            System.out.println("Usuário não encontrado!");
            return;
        }
        
        if (user.equals(currentUser)) {
            System.out.println("Você não pode remover seu próprio usuário!");
            return;
        }
        
        System.out.print("Tem certeza que deseja remover o usuário " + user.getName() + "? (S/N): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("S")) {
            users.remove(user);
            System.out.println("Usuário removido com sucesso!");
        } else {
            System.out.println("Operação cancelada.");
        }
    }
    
    // Helper methods
    private Request findRequestById(String id) {
        for (Request request : requests) {
            if (request.getId().startsWith(id)) {
                return request;
            }
        }
        return null;
    }
    
    private Acquisition findAcquisitionById(String id) {
        for (Acquisition acquisition : acquisitions) {
            if (acquisition.getId().startsWith(id)) {
                return acquisition;
            }
        }
        return null;
    }
    
    private User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    private boolean hasAcquisition(Request request) {
        for (Acquisition acquisition : acquisitions) {
            if (acquisition.getRequest().equals(request)) {
                return true;
            }
        }
        return false;
    }
}

// Model classes
enum UserRole {
    ADMIN("Administrador"),
    MANAGER("Gerente"),
    EMPLOYEE("Funcionário");
    
    private final String description;
    
    UserRole(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}

enum RequestStatus {
    PENDING("Pendente"),
    APPROVED("Aprovada"),
    REJECTED("Rejeitada");
    
    private final String description;
    
    RequestStatus(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}

enum AcquisitionStatus {
    IN_PROGRESS("Em Andamento"),
    DELIVERED("Entregue"),
    CANCELLED("Cancelada");
    
    private final String description;
    
    AcquisitionStatus(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}

class User {
    private String username;
    private String password;
    private String name;
    private UserRole role;
    
    public User(String username, String password, String name, UserRole role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
}

class Request {
    private String id;
    private String title;
    private String description;
    private double estimatedValue;
    private User requester;
    private RequestStatus status;
    private LocalDate creationDate;
    
    public Request(String id, String title, String description, double estimatedValue, User requester, RequestStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.estimatedValue = estimatedValue;
        this.requester = requester;
        this.status = status;
        this.creationDate = LocalDate.now();
    }
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public double getEstimatedValue() {
        return estimatedValue;
    }
    
    public User getRequester() {
        return requester;
    }
    
    public RequestStatus getStatus() {
        return status;
    }
    
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
    
    public LocalDate getCreationDate() {
        return creationDate;
    }
}

class Acquisition {
    private String id;
    private Request request;
    private LocalDate acquisitionDate;
    private String supplier;
    private double actualValue;
    private AcquisitionStatus status;
    
    public Acquisition(String id, Request request, LocalDate acquisitionDate, String supplier, double actualValue, AcquisitionStatus status) {
        this.id = id;
        this.request = request;
        this.acquisitionDate = acquisitionDate;
        this.supplier = supplier;
        this.actualValue = actualValue;
        this.status = status;
    }
    
    public String getId() {
        return id;
    }
    
    public Request getRequest() {
        return request;
    }
    
    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }
    
    public String getSupplier() {
        return supplier;
    }
    
    public double getActualValue() {
        return actualValue;
    }
    
    public AcquisitionStatus getStatus() {
        return status;
    }
    
    public void setStatus(AcquisitionStatus status) {
        this.status = status;
    }
}

// Execute the main method to run the application
ProcurementSystem.main(new String[]{});