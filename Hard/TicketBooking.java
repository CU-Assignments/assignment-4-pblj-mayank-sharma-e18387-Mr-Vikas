import java.util.concurrent.locks.*;

class TicketBookingSystem {
    private int availableSeats;
    private final Lock lock = new ReentrantLock();

    public TicketBookingSystem(int seats) {
        this.availableSeats = seats;
    }

    public void bookTicket(String name, int seats) {
        lock.lock();
        try {
            if (availableSeats >= seats) {
                System.out.println(name + " successfully booked " + seats + " seat(s).");
                availableSeats -= seats;
            } else {
                System.out.println(name + " failed to book. Not enough seats available.");
            }
        } finally {
            lock.unlock();
        }
    }
}

class BookingThread extends Thread {
    private final TicketBookingSystem system;
    private final String customerName;
    private final int seats;

    public BookingThread(TicketBookingSystem system, String customerName, int seats, int priority) {
        this.system = system;
        this.customerName = customerName;
        this.seats = seats;
        setPriority(priority);
    }

    @Override
    public void run() {
        system.bookTicket(customerName, seats);
    }
}

public class TicketBooking {
    public static void main(String[] args) {
        TicketBookingSystem system = new TicketBookingSystem(5);

        BookingThread vip1 = new BookingThread(system, "VIP-1", 2, Thread.MAX_PRIORITY);
        BookingThread vip2 = new BookingThread(system, "VIP-2", 1, Thread.MAX_PRIORITY);
        BookingThread user1 = new BookingThread(system, "User-1", 2, Thread.NORM_PRIORITY);
        BookingThread user2 = new BookingThread(system, "User-2", 1, Thread.MIN_PRIORITY);

        vip1.start();
        vip2.start();
        user1.start();
        user2.start();
    }
}
