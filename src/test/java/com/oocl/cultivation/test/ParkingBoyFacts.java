package com.oocl.cultivation.test;

import com.oocl.cultivation.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParkingBoyFacts {

    //story1 AC1
    @Test
    void should_park_a_car_to_a_parking_lot_and_get_it_back() {
        //停车返票
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Car car = new Car();
        ParkingTicket ticket = parkingBoy.park(car);

        //给票取车
        Car fetched = parkingBoy.fetch(ticket);

        assertSame(fetched, car);
    }

    //story1 AC2
    @Test
    void should_park_multiple_cars_to_a_parking_lot_and_get_them_back() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Car firstCar = new Car();
        Car secondCar = new Car();

        ParkingTicket firstTicket = parkingBoy.park(firstCar);
        ParkingTicket secondTicket = parkingBoy.park(secondCar);

        Car fetchedByFirstTicket = parkingBoy.fetch(firstTicket);
        Car fetchedBySecondTicket = parkingBoy.fetch(secondTicket);

        assertSame(firstCar, fetchedByFirstTicket);
        assertSame(secondCar, fetchedBySecondTicket);
    }

    //story1 AC3
    @Test
    void should_not_fetch_any_car_once_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Car car = new Car();
        ParkingTicket wrongTicket = new ParkingTicket();

        ParkingTicket ticket = parkingBoy.park(car);

        assertNull(parkingBoy.fetch(null));
        assertNull(parkingBoy.fetch(wrongTicket));
        assertSame(car, parkingBoy.fetch(ticket));

    }

    //story1 AC4
    @Test
    void should_not_fetch_any_car_once_ticket_has_been_used() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = parkingBoy.park(car);
        parkingBoy.fetch(ticket);

        assertNull(parkingBoy.fetch(ticket));
    }

    //story1 AC5
    @Test
    void should_not_park_cars_to_parking_lot_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);

        parkingBoy.park(new Car());

        assertNull(parkingBoy.park(new Car()));
    }

    //story2 AC1
    @Test
    void should_query_message_once_the_ticket_is_wrong() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        ParkingTicket wrongTicket = new ParkingTicket();

        parkingBoy.fetch(wrongTicket);
        String message = parkingBoy.getLastErrorMessage();

        assertEquals("Unrecognized parking ticket.", message);
    }


    //story2 AC2
    @Test
    void should_query_message_once_ticket_is_not_provided() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);

        parkingBoy.fetch(null);

        assertEquals(
                "Please provide your parking ticket.",
                parkingBoy.getLastErrorMessage());
    }


    //Story2 AC3
    @Test
    void should_get_message_if_there_is_not_enough_position() {
        final int capacity = 1;
        ParkingLot parkingLot = new ParkingLot(capacity);
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);

        parkingBoy.park(new Car());
        parkingBoy.park(new Car());

        assertEquals("The parking lot is full.", parkingBoy.getLastErrorMessage());
    }


    //正常停车时错误消息应该置为空
    @Test
    void should_clear_the_message_once_the_operation_is_succeeded() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        ParkingTicket wrongTicket = new ParkingTicket();

        parkingBoy.fetch(wrongTicket);
        assertNotNull(parkingBoy.getLastErrorMessage());

        ParkingTicket ticket = parkingBoy.park(new Car());
        assertNotNull(ticket);
        assertNull(parkingBoy.getLastErrorMessage());
    }

    @Test
    void should_query_error_message_for_used_ticket() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingBoy parkingBoy = new ParkingBoy(parkingLot);
        Car car = new Car();

        ParkingTicket ticket = parkingBoy.park(car);
        parkingBoy.fetch(ticket);
        parkingBoy.fetch(ticket);

        assertEquals(
                "Unrecognized parking ticket.",
                parkingBoy.getLastErrorMessage()
        );
    }

    //story 3
    @Test
    void should_park_to_mutiple_parkingLots(){
        List<ParkingLot> parkingLots=new ArrayList<>();
        ParkingLot parkingLot1=new ParkingLot(1);
        ParkingLot parkingLot2=new ParkingLot(1);
        parkingLots.add(parkingLot1);
        parkingLots.add(parkingLot2);
        ParkingBoy parkingBoy=new ParkingBoy(parkingLots);

        Car car1=new Car();
        Car car2=new Car();
        ArrayList<Car> carList=new ArrayList<Car>();
        carList.add(car1);
        carList.add(car2);
        Map resultMap=parkingBoy.park(carList);
        assertSame(parkingLot1,resultMap.get(car1));
        assertSame(parkingLot2,resultMap.get(car2));

    }

    //story 4
    @Test
    void smart_parking_boy (){
        List<ParkingLot> parkingLots=new ArrayList<>();
        ParkingLot parkingLot1=new ParkingLot(1);
        ParkingLot parkingLot2=new ParkingLot(2);
        parkingLots.add(parkingLot1);
        parkingLots.add(parkingLot2);
        SmartParkingBoy smartParkingBoy=new SmartParkingBoy(parkingLots);

        Car car1=new Car();
        ParkingTicket ticket=smartParkingBoy.park(car1);

        //停车成功
        assertNotNull(ticket);
        //停在第二个停车场，则第二个停车场的size+1
        assertEquals(1,parkingLot2.getSize());
        //没有停在第一个停车场，所以第一个停车场的size为0
        assertEquals(0,parkingLot1.getSize());

    }

    //story 5
    @Test
    void super_smart_parking_boy(){
        List<ParkingLot> parkingLots=new ArrayList<>();
        ParkingLot parkingLot1=new ParkingLot(3);
        ParkingLot parkingLot2=new ParkingLot(2);
        parkingLot1.setSize(1);
        parkingLot2.setSize(1);
        parkingLots.add(parkingLot1);
        parkingLots.add(parkingLot2);
        SuperSmartParkingBoy superSmartParkingBoy=new SuperSmartParkingBoy(parkingLots);

        Car car1=new Car();
        ParkingTicket ticket=superSmartParkingBoy.park(car1);

        //停车成功
        assertNotNull(ticket);
        //没有停在第一个停车场，所以第一个停车场的size仍为1
        assertEquals(1,parkingLot2.getSize());
        //停在第二个停车场，则第二个停车场的size+1,应为2
        assertEquals(2,parkingLot1.getSize());
    }


    //story 6 AC1
    @Test
    void parkingManger_can_add_or_delete_parkingBoy (){
        ParkingLot managerParkingLot = new ParkingLot();
        ParkingManager parkingManager=new ParkingManager(managerParkingLot);
        ParkingLot parkingLot1=new ParkingLot();
        ParkingBoy parkingBoy=new ParkingBoy(parkingLot1);
        List<ParkingBoy> parkingBoyList=parkingManager.add(parkingBoy);

        assertEquals(true,parkingBoyList.contains(parkingBoy));

        parkingManager.delete(parkingBoy);
        assertEquals(false,parkingBoyList.contains(parkingBoy));
    }


    //story AC3
    @Test
    void parkingManager_order_parkingBoy_to_operate_and_return_message(){
        ParkingLot managerParkingLot = new ParkingLot();
        ParkingManager parkingManager=new ParkingManager(managerParkingLot);
        ParkingLot parkingLot1=new ParkingLot(1);
        ParkingBoy parkingBoy=new ParkingBoy(parkingLot1);
        List<ParkingBoy> parkingBoyList=parkingManager.add(parkingBoy);
        Car car1=new Car();
        Car car2=new Car();
        ParkingTicket parkingTicket=parkingManager.orderParkCar(parkingBoy,car1);
        assertNotNull(parkingTicket);

        //超过容量，第二辆车无法停入
        ParkingTicket parkingTicket1=parkingManager.orderParkCar(parkingBoy,car2);
        assertEquals("The parking lot is full.",parkingManager.getLastErrorMessage());

        //取车case

        assertSame(car1,parkingManager.orderFetch(parkingBoy,parkingTicket));
       // assertEquals("Unrecognized parking ticket.",parkingManager.getLastErrorMessage());
        parkingManager.orderFetch(parkingBoy,null);
        assertEquals("Please provide your parking ticket.",parkingManager.getLastErrorMessage());



    }

}




