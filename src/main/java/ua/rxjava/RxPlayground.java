package ua.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Песочница для примеров RxJava.
 * Observable генерирует трафик, event, информацию, событие, сообщения, месседжи, сигналы -> observer
 * Hot observable генерирует события вне зависимости подписан на него какойто observer или нет
 * Cold observable генерирует события только при наличии одного подписчика
 *
 * timer это Hot (он всегда пушит события)
 * Cold (пассивный пока на него ктото не подпишется)- это те которые мы создаем руками, генераторами
 *
 * backpressure -  сними борются операторы filter, debounce, buffer, window
 *
 * надо разобраться в event publisher
 * ставлю цепочку различных обработчиков они называются операторами в терминах RX java
 * <p>
 * CommandLineRunner выполняется один раз, после того как контекст Spring
 * полностью поднят. Ненужные примеры достаточно закомментировать в run().
 */
@Component
public class RxPlayground implements CommandLineRunner {

	/** Сюда складываются все подписки, которые не завершаются сами. */
	private final CompositeDisposable disposables = new CompositeDisposable();

	@Override
	public void run(String... args) {
//		this.justExample();
//		this.fromExample();
		this.rangeExample();
//		this.fullObserver();
//		this.shortForm();
//		this.operatorChain();
//		this.combineLatest();
	}

	/**
	 * just — перечисляешь готовые значения прямо в коде.
	 * Метода Observable.of в RxJava нет: аналог Stream.of называется just.
	 */
	private void justExample() {
		System.out.print("[just] ");
		Observable.just("Кинг", "Оруэлл", "Толкин")
				.subscribe(v -> System.out.print(v + " | "));
		System.out.println();

		// Ловушка: один аргумент-список даёт ОДНО значение (сам список),
		// а не три отдельных. Для списка нужен fromIterable.
		Observable.just(List.of("a", "b", "c"))
				.subscribe(v -> System.out.println("[just] один элемент-список: " + v));
	}

	/** from* — источником становится то, что уже существует. */
	private void fromExample() {
		// Из коллекции: каждый элемент — отдельное значение.
		System.out.print("[fromIterable] ");
		Observable.fromIterable(List.of("Кинг", "Оруэлл", "Толкин"))
				.subscribe(v -> System.out.print(v + " | "));
		System.out.println();

		// Из массива.
		System.out.print("[fromArray] ");
		Observable.fromArray(1, 2, 3)
				.subscribe(v -> System.out.print(v + " "));
		System.out.println();

		// Из функции: она выполняется заново на КАЖДУЮ подписку.
		AtomicInteger counter = new AtomicInteger();
		Observable<Integer> lazy = Observable.fromCallable(counter::incrementAndGet);
		lazy.subscribe(v -> System.out.println("[fromCallable] подписка 1 -> " + v));
		lazy.subscribe(v -> System.out.println("[fromCallable] подписка 2 -> " + v));

		// Для сравнения: just вычисляет значение ОДИН раз, ещё до подписки,
		// поэтому обе подписки получают одно и то же число.
		AtomicInteger counter2 = new AtomicInteger();
		Observable<Integer> eager = Observable.just(counter2.incrementAndGet());
		eager.subscribe(v -> System.out.println("[just] подписка 1 -> " + v));
		eager.subscribe(v -> System.out.println("[just] подписка 2 -> " + v));
	}

	/** range — последовательность целых чисел. */
	private void rangeExample() {
		System.out.print("[range] range(1, 5): ");
		Observable.range(1, 5).subscribe(v -> System.out.print(v + " "));
		System.out.println();

		// Второй аргумент — КОЛИЧЕСТВО значений, а не последнее число.
		System.out.print("[range] range(10, 3): ");
		Observable.range(10, 3).subscribe(v -> System.out.print(v + " "));
		System.out.println();
	}

	/** Полная форма Observer: видно все четыре метода контракта. */
	private void fullObserver() {
		// Источник. Здесь ещё НИЧЕГО не происходит: Observable только
		// описывает, что нужно будет сделать.
		Observable<Integer> numbers = Observable.range(1, 15);
		System.out.println("\n[1] Observable создан, но пока не подписались — вывода нет");

		numbers.subscribe(new Observer<Integer>() {

			@Override
			public void onSubscribe(Disposable d) {
				// Один раз, до первого значения. Disposable — ручка отмены.
				System.out.println("onSubscribe: подписка установлена");
			}

			@Override
			public void onNext(Integer value) {
				System.out.print(value + " ");
			}

			@Override
			public void onError(Throwable e) {
				// После onError не будет ни onNext, ни onComplete.
				System.out.println("onError: " + e.getMessage());
			}

			@Override
			public void onComplete() {
				System.out.println("\nonComplete: поток завершён");
			}
		});
	}

	/** То же самое, но Observer собирается из лямбды. */
	private void shortForm() {
		System.out.print("[2] Короткая форма: ");
		Observable.range(1, 15).subscribe(value -> System.out.print(value + " "));
		System.out.println();
	}

	/** Цепочка операторов поверх бесконечного interval. */
	private void operatorChain() {
		System.out.println("[3] Цепочка операторов:");

		Disposable subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
				.filter(x -> x >= 10)
				.filter(x -> x <= 20)
				.skip(2)
				.scan((x, y) -> x + y)
				.subscribe(
						v -> System.out.println("Received: " + v),
						e -> System.out.println("Error: " + e),
						() -> System.out.println("Completed")
				);

		// interval бесконечен, а filter(x <= 20) просто перестаёт пропускать
		// значения после 20-го тика — поэтому onComplete НЕ придёт никогда,
		// поток замолчит и продолжит тикать вхолостую.
		// Чтобы он завершился штатно, замени filter(x -> x <= 20)
		// на takeWhile(x -> x <= 20) — тогда сработает "Completed".
		disposables.add(subscription);
	}

	/**
	 * Вызывается Spring при остановке контекста, до завершения JVM.
	 * dispose() обрывает все накопленные подписки разом.
	 */
	@PreDestroy
	void shutdown() {
		System.out.println("@PreDestroy: активных подписок = " + disposables.size());
		disposables.dispose();
		System.out.println("@PreDestroy: отменены, isDisposed = " + disposables.isDisposed());
	}

	/** combineLatest: срабатывает на значение из любого источника. */
	private void combineLatest() {
		System.out.println("[4] combineLatest:");

		Observable<Long> x = Observable.interval(100, TimeUnit.MILLISECONDS).take(8);
		Observable<Long> y = Observable.interval(350, TimeUnit.MILLISECONDS).take(3);

		// Третий аргумент задаёт тип результата: здесь Long, потому что vx + vy.
		Observable<Long> values2 = Observable.combineLatest(x, y, (vx, vy) -> vx + vy);

		values2.subscribe(
				sum -> System.out.println("combineLatest -> " + sum),
				e -> System.out.println("Error: " + e),
				() -> System.out.println("combineLatest Completed")
		);
	}

}
