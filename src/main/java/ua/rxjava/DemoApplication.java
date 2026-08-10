package ua.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import org.reactivestreams.Subscription;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class DemoApplication {

	static void main(String[] args) throws IOException {
		SpringApplication.run(DemoApplication.class, args);

		// ШАГ 1. Источник данных. Здесь ещё НИЧЕГО не происходит:
		// Observable только описывает, что нужно будет сделать.
		Observable<Integer> numbers = Observable.range(1, 15);

		System.out.println("Observable создан, но пока не подписались — вывода нет");

		// ШАГ 2. Потребитель в полной форме: видно все четыре метода контракта.
//		numbers.subscribe(new Observer<Integer>() {
//
//			@Override
//			public void onSubscribe(Disposable d) {
//				// Вызывается один раз, до первого значения.
//				// Disposable — ручка для отмены: d.dispose() прервёт поток.
//				System.out.println("onSubscribe: подписка установлена");
//			}
//
//			@Override
//			public void onNext(Integer value) {
//				// Вызывается на каждое значение: здесь ровно 15 раз.
//				System.out.println("onNext: " + value);
//			}
//
//			@Override
//			public void onError(Throwable e) {
//				// Аварийное завершение. После него onNext и onComplete не будет.
//				System.out.println("onError: " + e.getMessage());
//			}
//
//			@Override
//			public void onComplete() {
//				// Нормальное завершение — значений больше не будет.
//				System.out.println("onComplete: поток завершён");
//			}
//		});

		// ШАГ 3. Тот же результат в короткой форме: RxJava сам соберёт Observer
		// из лямбды. onError и onComplete при этом опускаются.
		System.out.print("Короткая форма: ");
		numbers.subscribe(value -> System.out.print(value + " "));
		System.out.println();


		Observable<Long> values = Observable.interval(100, TimeUnit.MILLISECONDS);

		Subscription subscription = (Subscription) values.subscribe(
			v -> System.out.println("Received: " + v),
			e -> System.out.println("Error: " + e),
			() -> System.out.println("Completed")
		);

		System.in.read();
	}

}
